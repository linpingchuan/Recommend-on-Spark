package com.moon.redis

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.junit.Test
import redis.clients.jedis.JedisPool

/**
  * Created by lin on 4/24/16.
  */
class InsertRedisTest {
  @Test
  def testInsert(): Unit ={

    object RedisClient extends Serializable{
      val redisHost="localhost"
      val redisPort=6379
      val redisTimeout=30000
      lazy val pool=new JedisPool(new GenericObjectPoolConfig(),redisHost,redisPort,redisTimeout)
      lazy val hook=new Thread{
        override def run={
          println("Execute hook thread: "+this)
          pool.destroy()
        }
      }
      sys.addShutdownHook(hook.run)
    }
    val dbIndex=1
    val clickHashKey="app::user::click"
    val jedis=RedisClient.pool.getResource
    jedis.select(dbIndex)
    jedis.hincrBy(clickHashKey,"111111",12)
    RedisClient.pool.returnResource(jedis)
  }
}
