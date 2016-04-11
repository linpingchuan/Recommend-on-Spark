package com.moon.entity

/**
  * Created by lin on 3/13/16.
  */
class ShopMenu {
  var id:Long =_
  var shop:Shop=_
  var name:String=_
  var intro:String=_
}
object ShopMenu{
  def apply(id:Long,shop:Shop,name:String,intro:String): Unit ={
    val s=new ShopMenu
    s.id=id
    s.shop=shop
    s.name=name
    s.intro=intro
  }
}