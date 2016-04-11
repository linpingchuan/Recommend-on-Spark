package com.moon.entity

import java.util.Date

/**
  * Created by lin on 3/13/16.
  */
class ToRecommend extends Serializable{
  var id :Long = _
  var customer :Customer= _
  var shopMenu :ShopMenu = _
  var createAt:Date=_
}

object ToRecommend{
  def apply(id:Long,customer:Customer,shopMenu:ShopMenu,createAt:Date): Unit ={
    val t=new ToRecommend
    t.id=id
    t.customer=customer
    t.shopMenu=shopMenu
    t.createAt=createAt
  }
}
