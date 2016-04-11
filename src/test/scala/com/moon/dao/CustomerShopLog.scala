package com.moon.dao

import com.moon.config.Config

import scala.io.Source

/**
  * Created by lin on 3/9/16.
  */
object CustomerShopLog {
  def main(args:Array[String]): Unit ={
    import2db()
  }
  def import2db(): Unit ={
    val db = Config.persistenceContext
    Source.fromFile("/home/lin/Downloads/profiledata/user_artist_data.txt").getLines().foreach{line =>
      val Array(userID,artistID,count)=line.split(' ').map(_.toInt)

    }

  }
}
