package com.moon.dao.redis

/**
  * Created by lin on 4/30/16.
  */
object UserBehaviorDao {
  def insertRawMenuData(menuId:Int,menuName:String): Unit ={
    JedisUtils.jedis.hincrBy("app::user::RawMenuData",menuName,menuId)
  }

  def insertMenuAlias(menuId:Int,menuAlias:Int): Unit ={
    JedisUtils.jedis.hincrBy("app::user::MenuAlias",String.valueOf(menuId),menuAlias)
  }

  def insertUserMenu(userId:Int,menuId:Int,data:Int): Unit ={
    JedisUtils.jedis.hincrBy("app::user::UserMenu",new StringBuilder().append(userId).append(":").append(menuId).toString,data)
  }

  def queryMenuAlias()={
    JedisUtils.jedis.hgetAll("app::user::MenuAlias")
  }
}
