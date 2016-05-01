package com.moon.kafka

import java.util.Properties

import kafka.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.Consumer

/**
  * Created by lin on 4/24/16.
  */
object KafkaEventConsumer {
  def main(args: Array[String]): Unit = {
    val topics = Set("user_events")
    val brokers = "localhost:9092"
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")

    val kafkaConfig = new ConsumerConfig(props)
  }
}
