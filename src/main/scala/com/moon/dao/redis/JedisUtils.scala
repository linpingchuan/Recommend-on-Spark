package com.moon.dao.redis

import redis.clients.jedis.Jedis

/**
  * Created by lin on 4/30/16.
  */
object JedisUtils {
  val jedis:Jedis = new Jedis("localhost",6379)
}
