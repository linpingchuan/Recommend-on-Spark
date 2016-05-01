package com.moon.dao.redis

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{JedisPool, Jedis}

/**
  * Created by lin on 4/30/16.
  */
object JedisUtils {
  val jedis:Jedis = new Jedis("localhost",6379)
  val config:GenericObjectPoolConfig=new GenericObjectPoolConfig()
  val pool:JedisPool=new JedisPool(config,"localhost",6379,100000)
}
