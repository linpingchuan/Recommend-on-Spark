package com.moon.app

import java.util

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{StreamingContext, Seconds}

/**
  * Created by lin on 4/12/16.
  */
object KafkaApp {
  def main(args:Array[String]): Unit ={
    val array=new Array[String](1)

    array(0)="/home/lin/IdeaProjects/moon/target/recommend-1.0-SNAPSHOT-jar-with-dependencies.jar"

    val conf=new SparkConf().setAppName("kafkaApp").setMaster("local[*]").setJars(array)
    val ssc=new StreamingContext(conf,Seconds(1))

    val kafkaParams=Map(
      "zookeeper.connect"->"localhost:2181",
      "zookeeper.connection.timeout.ms"->"10000",
      "group.id"->"sparkGroup"
    )

    val topics=Map(
      "test"->1
    )

    // stream of (topic,ImpressionLog)
    val messages=KafkaUtils.createStream(ssc,kafkaParams,topics,StorageLevel.MEMORY_AND_DISK)
    println(s"Number of words: %{messages.count()}")
  }
}
