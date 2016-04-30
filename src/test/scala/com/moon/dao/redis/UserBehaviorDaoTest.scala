package com.moon.dao.redis

import org.junit.Test

import scala.io.Source

/**
  * Created by lin on 4/30/16.
  */
class UserBehaviorDaoTest {
  @Test
  def testInsertMenuData(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/artist_data.txt").getLines().foreach( x =>{
      try{
        val (id,name) =x.span(_ != '\t')
        UserBehaviorDao.insertRawMenuData(id.toInt,name.trim)
      }catch{
        case e:Exception => println(e.getCause)
      }
    })
  }
  @Test
  def testMenuAlias(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/artist_alias.txt").getLines().foreach( x =>{
      try{
        val tokens=x.split('\t')
        if(tokens(0).isEmpty){
          None
        }else{
          UserBehaviorDao.insertMenuAlias(tokens(0).toInt,tokens(1).toInt)
        }
      }catch{
        case e:Exception => println(e.getCause)
      }
    })
  }
  @Test
  def testUserMenu(): Unit ={
    Source.fromFile("/home/lin/Downloads/profiledata/user_artist_data.txt").getLines().foreach( x =>{
      try{
        val Array(userID,artistID,count)=x.split(' ').map(_.toInt)
        val finalArtistID=UserBehaviorDao.queryMenuAlias().getOrDefault(artistID,String.valueOf(artistID)).toInt
        UserBehaviorDao.insertUserMenu(userID,finalArtistID,count)
      }catch{
        case e:Exception => println(e.getCause)
      }
    })
  }
  @Test
  def test(): Unit ={
    println(UserBehaviorDao.queryMenuAlias().getOrDefault(10229949,String.valueOf("10229949"))
  }
}
