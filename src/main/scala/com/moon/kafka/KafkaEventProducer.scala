package com.moon.kafka

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import net.sf.json.JSONObject

import scala.io.Source

/**
  * Created by lin on 4/21/16.
  */
object KafkaEventProducer {
  val rawMenuData = Source.fromFile("/home/lin/Downloads/profiledata/artist_data.txt").getLines().flatMap(x => {
    val(id,name) = x.span(_ != '\t')
    if(name.isEmpty){
      None
    }else{
      try{
        Some(id.toInt,name.trim)
      }catch{
        case e:NumberFormatException => None
      }
    }
  })

  val rawMenuAlias=Source.fromFile("/home/lin/Downloads/profiledata/artist_alias.txt").getLines().flatMap(x => {
    val tokens = x.split('\t')
    if(tokens(0).isEmpty){
      None
    }else{
      Some((tokens(0).toInt,tokens(0).toInt))
    }
  })

  val trainData=Source.fromFile("/home/lin/Downloads/profiledata/user_artist_data.txt").getLines().map(x => {
    val Array(userId,menuId,count) = x.split(' ').map(_.toInt)
//    val finalMenuId = rawMenuAlias
    (userId,menuId,count)
  })

  def main(args:Array[String]): Unit ={
    val topic="user_events"
    val brokers="localhost:9092"
    val props=new Properties()
    props.put("metadata.broker.list",brokers)
    props.put("serializer.class","kafka.serializer.StringEncoder")
    val kafkaConfig=new ProducerConfig(props)
    val producer=new Producer[String,String](kafkaConfig)

    trainData.foreach(x => {
      val event=new JSONObject()
      event.put("userId",x._1)
      event.put("menuId",x._2)
      event.put("count",x._3)
      producer.send(new KeyedMessage[String,String](topic,event.toString()))
      //Thread.sleep(2000)
    })
  }

}
