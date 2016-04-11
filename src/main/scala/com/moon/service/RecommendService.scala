package com.moon.service

import com.moon.config.Config
import com.moon.dao.UserBehaviorDao
import com.moon.entity.{Customer, ShopMenu, UserBehavior}
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import scala.collection.Map
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by lin on 3/9/16.
  */
object RecommendService {
  val log=LoggerFactory.getLogger(RecommendService.getClass);
  val db=Config.persistenceContext
  def buildArtistAlias(rawArtistAlias: RDD[String]): Map[Int, Int] =
    rawArtistAlias.flatMap { line =>
      val tokens = line.split('\t')
      if (tokens(0).isEmpty) {
        None
      } else {
        Some((tokens(0).toInt, tokens(1).toInt))
      }
    }.collectAsMap()



  def buildRatings(rawUserArtistData: RDD[String],
                    bArtistAlias: Broadcast[Map[Int, Int]]) = {
    rawUserArtistData.map { line =>
      val Array(userID, artistID, count) = line.split(' ').map(_.toInt)
      val finalArtistID = bArtistAlias.value.getOrElse(artistID, artistID)
      Rating(userID, finalArtistID, count)
    }
  }
  def buildRatings(rawCustomerMenuData:DataFrame) ={
    rawCustomerMenuData.map{x =>
      Rating(x.getAs[Long]("user_behavior_fk_customer_id").toInt,
        x.getAs[Long]("user_behavior_fk_shop_menu_id").toInt,x.getAs[Long]("browse_shop_count").toDouble)
    }
  }
  def model(rawCustomerMenuData:DataFrame,rawMenuData:DataFrame,userID:Int): Unit ={
    val trainData=buildRatings(rawCustomerMenuData).cache()

    val model = ALS.trainImplicit(trainData, 10, 5, 0.01, 1.0)

    trainData.unpersist()

    val recommendations=model.recommendProducts(userID,5)
    recommendations.foreach(x =>{
      log.info("(CustomerID,MenuID,Rating) => {}",x)
      insert(x.user,x.product,x.rating)
    })
  }
  def insert(customerId:Long,shopMenuId:Long,userShopMenuScore:Double): Unit ={
    val db=Config.persistenceContext
    db.transaction{implicit session=>
      val shopMenu=new ShopMenu
      shopMenu.id=shopMenuId
      val customer=new Customer
      customer.id=customerId
      val behavior=new UserBehavior
      behavior.shopMenu=shopMenu
      behavior.customer=customer
      behavior.userShopMenuScore=userShopMenuScore
      UserBehaviorDao insert behavior
    }
  }

  def areaUnderCurve(
                      positiveData: RDD[Rating],
                      bAllItemIDs: Broadcast[Array[Int]],
                      predictFunction: (RDD[(Int, Int)] => RDD[Rating])) = {
    val positiveUserProducts = positiveData.map(r => (r.user, r.product))
    val positivePredictions = predictFunction(positiveUserProducts).groupBy(_.user)
    val negativeUserProducts = positiveUserProducts.groupByKey().mapPartitions {
      userIDAndPosItemIDs => {
        val random = new Random()
        val allItemIDs = bAllItemIDs.value
        userIDAndPosItemIDs.map { case (userID, posItemIDs) =>
          val posItemIDSet = posItemIDs.toSet
          val negative = new ArrayBuffer[Int]()
          var i = 0
          while (i < allItemIDs.size && negative.size < posItemIDSet.size) {
            val itemID = allItemIDs(random.nextInt(allItemIDs.size))
            if (!posItemIDSet.contains(itemID)) {
              negative += itemID
            }
            i += 1
          }
          negative.map(itemID => (userID, itemID))
        }
      }
    }.flatMap(t => t)
    val negativePredictions = predictFunction(negativeUserProducts).groupBy(_.user)
    positivePredictions.join(negativePredictions).values.map {
      case (positiveRatings, negativeRatings) =>
        var correct = 0L
        var total = 0L
        for (positive <- positiveRatings;
             negative <- negativeRatings) {
          if (positive.rating > negative.rating) {
            correct += 1
          }
          total += 1
        }
        correct.toDouble / total
    }.mean()
  }

  def predictMostListened(sc: SparkContext, train: RDD[Rating])(allData: RDD[(Int, Int)]) = {
    val bListenCount =
      sc.broadcast(train.map(r => (r.product, r.rating)).reduceByKey(_ + _).collectAsMap())
    allData.map { case (user, product) =>
      Rating(user, product, bListenCount.value.getOrElse(product, 0.0))
    }
  }

  def evaluate(
                sc: SparkContext,
                rawCustomerMenuData:DataFrame): Unit = {

    val allData = buildRatings(rawCustomerMenuData)
    val Array(trainData, cvData) = allData.randomSplit(Array(0.9, 0.1))
    trainData.cache()
    cvData.cache()

    val allItemIDs = allData.map(_.product).distinct().collect()
    val bAllItemIDs = sc.broadcast(allItemIDs)

    val evaluations =
      for (rank <- Array(10, 50);
           lambda <- Array(1.0, 0.0001);
           alpha <- Array(1.0, 40.0))
        yield {
          val model = ALS.trainImplicit(trainData, rank, 10, lambda, alpha)
          val auc = areaUnderCurve(cvData, bAllItemIDs, model.predict)
          unpersist(model)
          ((rank, lambda, alpha), auc)
        }
    trainData.unpersist()
    cvData.unpersist()
  }

  def recommend(
                 sc: SparkContext,
                 rawCustomerMenuData:DataFrame,rawMenuData:DataFrame,userID:Int): Unit = {

    val allData = buildRatings(rawCustomerMenuData).cache()
    val model = ALS.trainImplicit(allData, 50, 10, 1.0, 40.0)
    allData.unpersist()

    val recommendations = model.recommendProducts(userID, 5)
    recommendations.foreach(x =>{
      log.info("(CustomerID,MenuID,Rating) => {}",x)
      insert(x.user,x.product,x.rating)
    })
  }

  def unpersist(model: MatrixFactorizationModel): Unit = {
    // At the moment, it's necessary to manually unpersist the RDDs inside the model
    // when done with it in order to make sure they are promptly uncached
    model.userFeatures.unpersist()
    model.productFeatures.unpersist()

  }
}
