package com.moon.dao

import com.moon.config.Config
import com.moon.entity.{ShopMenu, UserBehavior, Customer, Shop}
import org.junit.Test

import scala.io.Source

/**
  * Created by lin on 3/9/16.
  */
class UserBehaviorDaoTest {
  @Test
  def testInsert(): Unit = {
    insert(139,23,0)
  }
  @Test
  def batchInsert(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/user_artist_data.txt").getLines().foreach( x =>{
      try{
//        println(x.split(' ')(0).toLong+" "+x.split(' ')(1).toLong)
        insert(x.split(' ')(0).toLong,x.split(' ')(1).toLong,0)
      }catch{
        case e:Exception => println(e.getCause)
      }

    })
  }
  def insert(customerId:Long,shopMenuId:Long,userShopMenuScore:Double): Unit ={
    val db=Config.persistenceContext
    db.transaction{implicit session=>
      val shopMenu=new ShopMenu
      shopMenu.id=shopMenuId
      val customer=new Customer
      customer.id=customerId
      val behavior=new UserBehavior
      behavior.shopMenu=shopMenu
      behavior.customer=customer
      behavior.userShopMenuScore=userShopMenuScore
      UserBehaviorDao insert behavior
    }
  }

}
