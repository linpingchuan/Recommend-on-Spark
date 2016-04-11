package com.moon.config

import com.moon.dao._
import org.mybatis.scala.config.Configuration

/**
  * Created by lin on 3/8/16.
  */
object Config {
  // Load datasource configuration
  val config = Configuration("mybatis.xml")

  // Create a configuration space,add the data access method
  config.addSpace("item"){space =>
    space ++= UserBehaviorDao
    space ++= ShopDao
    space ++= CustomerDao
    space ++= MoneyDao
    space ++= ToRecommendDao
    space ++= ShopMenuDao
  }

  // Build the session manager
  val persistenceContext = config.createPersistenceContext
}
