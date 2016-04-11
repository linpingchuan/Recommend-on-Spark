package com.moon.dao

import java.util.Date

import com.moon.config.Config
import com.moon.entity.{Money, Shop}

import scala.io.Source

/**
  * Created by lin on 3/9/16.
  */
object ShopDaoTest {
  def main(args:Array[String]): Unit = {
    batchInsert()
  }
  def batchInsert(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/artist_data.txt").getLines().foreach{line =>
      val (id,name)=line.span(_!='\t')
      if(name.isEmpty){
        None
      }else{
        try{
          insert(id.toLong,name.trim)
        }catch{
          case e:NumberFormatException => None
        }
      }
    }
  }
  def insert(id:Long,name:String): Unit ={
    try{
      val db=Config.persistenceContext
      db.transaction{implicit session =>
        val s=new Shop
        s.name=name
        s.id=id
        ShopDao insert s
      }
    }catch{
      case e:Exception => println(e.getCause)
    }

  }
}
