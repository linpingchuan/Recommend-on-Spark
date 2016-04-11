package com.moon.entity

import java.util.Date

/**
  * Created by lin on 3/8/16.
  */
class Shop extends Serializable{
  var id: Long = _
  var name: String = _
  var address:  String = _
  var tel:  String = _
  var boss:  String = _
  var intro:  String = _
  var establishTime: Date = _
  var pass:  String = _
  var status: Int = _
  var money: Money = _
}

object shop {
  def apply(id: Long, name: String , address: String,
            tel: String, boss: String, intro: String,
            establishTime: Date, pass: String,
            status: Int, money: Money): Unit = {
    val s = new Shop
    s.id = id
    s.name = name
    s.address = address
    s.tel = tel
    s.boss = boss
    s.intro = intro
    s.establishTime = establishTime
    s.pass = pass
    s.status = status
    s.money = money
  }
}
