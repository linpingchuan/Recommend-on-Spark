package com.moon.entity

import java.util.Date

/**
  * Created by lin on 3/8/16.
  */
class Customer extends Serializable{
  var id: Long = _
  var pass: String = _
  var establishTime: Date = _
  var avatar: String = _
  var sex: Int = _
  var nickname: String = _
  var liveAddress: String = _
  var loginTime: Date = _
  var ip: String = _
  var signnature: String = _
  var email: String = _
  var phone: String = _
  var money: Money = _
}

object Customer {
  def apply(id: Long, pass: String, establishTime: Date, avatar: String,
            sex: Int, nickname: String, liveAddress: String,
            loginTime:Date,ip:String,signnauture:String,email:String,
            phone:String,money:Money): Unit = {
    val c = new Customer
    c.id=id
    c.pass=pass
    c.establishTime=establishTime
    c.avatar=avatar
    c.sex=sex
    c.nickname=nickname
    c.liveAddress=liveAddress
    c.loginTime=loginTime
    c.ip=ip
    c.signnature=signnauture
    c.email=email
    c.phone=phone
    c.money=money
  }
}
