package com.moon.kafka

import kafka.serializer.StringDecoder
import net.sf.json._
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory
import redis.clients.jedis.{Jedis, JedisPool}

/**
  * Created by lin on 4/10/16.
  */
object UserClickCountAnalytics {
  val LOG=LoggerFactory.getLogger(UserClickCountAnalytics.getClass)

  def main(args:Array[String]): Unit ={
    // Create a StreamingContext with the given master URL
    val conf=new SparkConf().setAppName("Recommender")
    val ssc=new StreamingContext(conf,Seconds(1))

    // Kafka configurations
    val topics=Set("user_view_topics")
    val brokers="localhost:9092"
    val kafkaParams=Map[String,String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder"
    )

    val clickHashKey="app::users::click"

    // Create a direct stream
    val kafkaStream=KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)

    val events=kafkaStream.flatMap(line => {
        val data=JSONObject.fromObject(line._2)
        Some(data)
    })
    // Compute user click times
    val userClicks=events.map(x => (x.getString("uid"),x.getInt("click_count"))).reduceByKey(_ + _)

    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair =>{
            val uid=pair._1
            val clickCount=pair._2
            println("uid: "+uid+" clickCount: "+clickCount)
            LOG.info("uid: {}",uid)
            val jedis=new Jedis("localhost",6379)
            jedis.hincrBy(clickHashKey,uid,clickCount)
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
