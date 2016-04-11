package com.moon.app

import java.util

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, StreamingContext, Seconds}

import org.apache.kafka.clients.producer._
/**
  * Created by lin on 4/10/16.
  */
object KafkaWordCount {
  def main(args:Array[String]): Unit ={
    val array=new Array[String](4)
    array(0)="zoo01,zoo02,zoo03"
    array(1)="my-consumer-group"
    array(2)="topic1,topic2"
    array(3)="1"

    val Array(zkQuorum,group,topics,numThreads)=array

    val sparkConf=new SparkConf().setAppName("KafkaWordCount")
    val ssc=new StreamingContext(sparkConf,Seconds(2))
    ssc.checkpoint("hdfs://localhost:9000/data")

    val topicMap=topics.split(",").map((_ , numThreads.toInt)).toMap
    val lines=KafkaUtils.createStream(ssc,zkQuorum,group,topicMap).map(_._2)

    val words=lines.flatMap(_.split(" "))
    val wordCounts=words.map(x => (x,1L)).reduceByKeyAndWindow(_ + _,_ - _,Minutes(10),Seconds(2),2)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }
}

// Produces some random words between 1 and 100
object KafkaWordCountProducer{
  def main(args:Array[String]): Unit ={
    val array=new Array[String](4)
    array(0)="zoo01,zoo02,zoo03"
    array(1)="my-consumer-group"
    array(2)="topic1,topic2"
    array(3)="1"

    val Array(brokers,topic,messagePerSec,wordsPerMessage)=array

    // Zookeeper connection properties
    val props=new util.HashMap[String,Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")

    val producer=new KafkaProducer[String,String](props)

    // Send some messages
    while(true){
      (1 to messagePerSec.toInt).foreach{ messageNum =>
        val str = (1 to wordsPerMessage.toInt).map(x => scala.util.Random.nextInt(10).toString).mkString(" ")

        val message=new ProducerRecord[String,String](topic,null,str)
        producer.send(message)
      }

      Thread.sleep(1000)
    }
  }
}
