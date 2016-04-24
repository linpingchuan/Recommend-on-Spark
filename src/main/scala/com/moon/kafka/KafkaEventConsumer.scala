package com.moon.kafka

/**
  * Created by lin on 4/24/16.
  */
object KafkaEventConsumer {
  def main(args:Array[String]): Unit ={
      val topics=Set("user_events")
      val brokers="localhost:9092"
      val kafkaParams=Map[String,String](
        "metadata.broker.list" -> brokers,
        "serializer.class"->"kafka.serializer.StringEncoder"
      )
  }
}
