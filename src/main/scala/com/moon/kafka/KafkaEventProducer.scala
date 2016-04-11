package com.moon.kafka

import scala.util.Random

/**
  * Created by lin on 4/10/16.
  */
object KafkaEventProducer {
  private val users = Array("4A4D769EB9679C054DE81B973ED5D768", "8dfeb5aaafc027d89349ac9a20b3930f",
    "011BBF43B89BFBF266C865DF0397AA71", "f2a8474bf7bd94f0aabbd4cdd2c06dcf",
    "068b746ed4620d25e26055a9f804385f", "97edfc08311c70143401745a03a50706",
    "d7f141563005d1b5d0d3dd30138f3f62", "c8ee90aade1671a21336c721512b817a",
    "6b67c8c700427dee7552f81f3228c927", "a95f22eabc4fd4b580c011a3161a9d9d")

  private val random = new Random()

  private var pointer = -1;

  def getUserID():String={
    pointer = pointer +1
    if(pointer > users.length){
      pointer = 0
      users(pointer)
    }else{
      users(pointer)
    }
  }

  def click():Double={
    random.nextInt(10)
  }
}
