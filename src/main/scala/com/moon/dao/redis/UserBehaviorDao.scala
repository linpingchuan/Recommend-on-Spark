package com.moon.dao.redis

/**
  * Created by lin on 4/30/16.
  */
object UserBehaviorDao {
  def insertRawMenuData(menuId: Int, menuName: String): Unit = {
    JedisUtils.pool.getResource.hincrBy("app::user::RawMenuData", menuName, menuId)

  }

  def insertMenuAlias(menuId: Int, menuAlias: Int): Unit = {
    JedisUtils.pool.getResource.hincrBy("app::user::MenuAlias", String.valueOf(menuId), menuAlias)
  }

  def insertUserMenu(userId: Int, menuId: Int, data: Int): Unit = {
    JedisUtils.pool.getResource.hincrBy("app::user::UserMenu", new StringBuilder().append(userId).append(":").append(menuId).toString, data)
  }

  def queryMenuAlias() = {
    JedisUtils.pool.getResource.hgetAll("app::user::MenuAlias")
  }

  def queryMenuAliasCount() = {
    JedisUtils.pool.getResource.hgetAll("app::user::MenuAlias").size()
  }

  def queryRawMenuDataCount() = {
    JedisUtils.pool.getResource.hgetAll("app::user::RawMenuData").size
  }

  def queryRawMenuData() = {
    JedisUtils.pool.getResource.hgetAll("app::user::RawMenuData")
  }

  def queryUserMenuCount = {
    JedisUtils.pool.getResource.hgetAll("app::user::UserMenu").size
  }

  def queryUserMenu = {
    JedisUtils.pool.getResource.hgetAll("app::user::UserMenu")
  }

  def insertRecommendMenu(userId: Int, menuId: Int) {
    JedisUtils.pool.getResource.hincrBy("app::user::RecommendMenu", String.valueOf(userId), menuId)
  }

  def queryRecommendMenu(userId:Int): Unit ={
    JedisUtils.pool.getResource.hget("app::user::RecommendMenu",String.valueOf(userId))
  }
}
