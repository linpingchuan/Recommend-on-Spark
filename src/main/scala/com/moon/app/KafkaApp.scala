package com.moon.app

import java.util
import java.util.UUID

import com.moon.dao.redis.{JedisUtils, UserBehaviorDao}
import kafka.serializer.StringDecoder
import net.sf.json.JSONObject
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.slf4j.LoggerFactory

/**
  * Created by lin on 4/12/16.
  */
object KafkaApp {
  val LOG = LoggerFactory.getLogger(KafkaApp.getClass)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Recommender").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(10))
    val topics = Set("user_events")
    val brokers = "localhost:9092"
    val kafkaParams = Map(
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder"
    )


    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    val events = kafkaStream.flatMap(line => {
      val data = JSONObject.fromObject(line._2)
      LOG.info("data : {}",data)
      Some(data)
    }).cache

    events.foreachRDD(rdd => {
      val trainData = rdd.map(x => {
        Rating(x.getInt("userId"), x.getInt("menuId"), x.getInt("count"))
      })

      val model = ALS.trainImplicit(trainData, 10, 5, 0.01, 1.0)

      val recommendProduct = model.recommendUsersForProducts(5)

      recommendProduct.foreach(x => {
        x._2.foreach(ratting => {
          LOG.info("recommendProduct : {}",ratting.product+":"+ratting.rating+":"+ratting.user)
          JedisUtils.pool.getResource.hincrBy("app::user::product", ratting.product + ":" + ratting.rating, ratting.user)
        })
      })

    })

    //
    //    val userEvents=events.map(x => ((x.getInt("userId"),x.getInt("menuId"),x.getInt("count"))))
    //    events.foreachRDD(rdd => {
    //      rdd.foreachPartition(partitionOfRecords => {
    //        partitionOfRecords.foreach(pair => {
    ////          println(pair._1)
    //        })
    //      })
    //      val trainData=rdd.map(x=>{
    //        Rating(x.getInt("userId"),x.getInt("menuId"),x.getInt("count"))
    //      })
    //      LOG.info("trainData count: {}",trainData.count())
    ////      val model=ALS.trainImplicit(trainData,10,5,0.01,1.0)
    ////      val recommendProduct=model.recommendProductsForUsers(5)
    ////      LOG.info("recommendProduct count: {}",recommendProduct.count())
    //    })

    ssc.start()
    ssc.awaitTermination()
  }
}
