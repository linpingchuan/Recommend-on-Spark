package com.moon.dao

import com.moon.config.Config
import com.moon.entity.Customer

import scala.io.Source

/**
  * Created by lin on 3/9/16.
  */
object CustomerDaoTest {
  def main(args:Array[String]): Unit ={
    batchInsert()
  }

  def batchInsert(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/user_artist_data.txt").getLines().foreach( x =>{
      try{
        insert(x.split(' ')(0).toLong)
      }catch{
        case e:Exception => println(e.getCause)
      }
    })


  }
  def insert(id:Long): Unit ={
    val db=Config.persistenceContext
    db.transaction{implicit session =>
      val c=new Customer
      c.id=id
      CustomerDao insert c
    }
  }
}
