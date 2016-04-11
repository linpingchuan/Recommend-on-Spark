package com.moon.dao

import java.util.Date

import com.moon.config.Config
import com.moon.entity.{ShopMenu, Customer, ToRecommend}
import org.mybatis.scala.session.RowBounds

/**
  * Created by lin on 3/13/16.
  */
object ToRecommendDaoTest {
  def main(args: Array[String]): Unit = {
    insert(2,1)
    insert(2,4)
    selectCustomerPage()
  }

  def selectCustomerPage(): Unit ={
    val db=Config.persistenceContext
    db.readOnly{implicit session=>
      val rows= ToRecommendDao findCustomerPage RowBounds(0,10)
      rows.foreach(x =>
        println(x.id+" : "+x.createAt +" "+x.customer.id+" "+x.shopMenu.id)
      )
    }
  }

  def insert(customerId:Long,shopMenuId:Long): Unit = {
    try{
      val db = Config.persistenceContext
      db.transaction { implicit session =>
        val t = new ToRecommend
        t.createAt = new Date
        val c = new Customer
        c.id = customerId
        val s = new ShopMenu
        s.id = shopMenuId
        t.customer=c
        t.shopMenu=s
        ToRecommendDao insert t
      }
    }catch {
      case e :Exception => println(e.getCause)
    }

  }

}
