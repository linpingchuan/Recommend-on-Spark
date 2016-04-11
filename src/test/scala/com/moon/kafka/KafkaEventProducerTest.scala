package com.moon.kafka

import java.util.Properties

import scala.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import org.codehaus.jettison.json.JSONObject
import org.junit.Test

/**
  * Created by lin on 4/10/16.
  */
class KafkaEventProducerTest {
  @Test
  def testKafkaEventProducer(): Unit ={
    val topic="user_events"
    val brokers="localhost:2181"
    val props=new Properties()
    props.put("metadata.broker.list",brokers)
    props.put("serializer.class","kafka.serializer.StringEncoder")

    val kafkaConfig=new ProducerConfig(props)
    val producer=new Producer[String,String](kafkaConfig)
    while(true){
      // prepare event data
      val event=new JSONObject()
      event.put("uid",KafkaEventProducer.getUserID)
        .put("event_time",System.currentTimeMillis.toString)
        .put("os_type","Android")
        .put("click_count",KafkaEventProducer.click)

      // produce event message
      producer.send(new KeyedMessage[String,String](topic,event.toString()))
      println("Message sent: "+event)

      Thread.sleep(200)
    }
  }
}
