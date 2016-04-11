package com.moon.dao

import com.moon.config.Config
import com.moon.entity.{Shop, UserBehavior, Customer, ShopMenu}
import org.junit.Test

import scala.io.Source

/**
  * Created by lin on 3/30/16.
  */
class ShopMenuDaoTest {
  @Test
  def testInsert(): Unit = {
    insert(35L,"name","intro")
  }

  @Test
  def testBatchInsert():Unit={
    Source.fromFile("/home/lin/Downloads/profiledata/artist_data.txt").getLines().foreach( x =>{
      try{
        val (id,name)=x.span( _ != '\t')
        if(name.isEmpty){
          insert(id.toInt,"unknown name","unknown intro")
        }else{
          insert(id.toInt,name.trim,"unknown intro")
        }
      }catch{
        case e:Exception => println(e.getCause)
      }

    })
  }

  def insert(shopMenuId: Long,name:String,intro:String): Unit = {
    val db = Config.persistenceContext
    db.transaction { implicit session =>
      val shop = new Shop
      shop.id = 13
      val shopMenu = new ShopMenu
      shopMenu.id = shopMenuId
      shopMenu.intro = intro
      shopMenu.name = name
      shopMenu.shop = shop
      ShopMenuDao insert shopMenu
    }
  }

}
