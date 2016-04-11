package com.moon.dao

import com.moon.config.Config
import com.moon.entity.Money

/**
  * Created by lin on 3/9/16.
  */
object MoneyDaoTest {
  def main(args:Array[String]): Unit ={
    val db=Config.persistenceContext
    db.transaction{implicit session=>
      val m=new Money
      m.sum=500000
      MoneyDao insert m
    }

  }
}
