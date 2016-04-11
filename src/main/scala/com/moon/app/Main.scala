package com.moon.app

import java.util.Properties
import java.util.concurrent.Executors
import com.moon.config.Config
import com.moon.dao.UserBehaviorDao
import com.moon.entity.{Shop, Customer, UserBehavior}
import com.moon.service.RecommendService
import com.moon.util.{RecommendAsyncClient, DataIOUtils}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

import scala.io.Source

/**
  * Created by lin on 3/8/16.
  */
object Main {
  val log = LoggerFactory.getLogger(Main.getClass)
  val url="jdbc:mysql://localhost:3306/moon"
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Recommender"))
    val prop=new Properties()
    prop.setProperty("user","root")
    prop.setProperty("password","root")
    val sqlContext=new SQLContext(sc)

    val rawCustomerMenuData=sqlContext.read.jdbc(url,"user_behavior",prop)
    val rawMenuData=sqlContext.read.jdbc(url,"shop_menu",prop)

    RecommendService.model(rawCustomerMenuData, rawMenuData,1000019)
    //RecommendService.evaluate(sc,rawCustomerMenuData)
//    RecommendService.recommend(sc,rawCustomerMenuData,rawMenuData,128)
  }
}
