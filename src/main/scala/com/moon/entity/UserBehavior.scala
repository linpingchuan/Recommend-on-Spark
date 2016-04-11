package com.moon.entity

/**
  * Created by lin on 3/8/16.
  */
class UserBehavior {
  var id:Long = _
  var customer:Customer = _
  var shopMenu:ShopMenu = _
  var browseShopCount:Long = _
  var userShopMenuScore:Double = _
}
object UserBehavior{
  def apply(id:Long,customer:Customer,shopMenu:ShopMenu,browseShopCount:Long,userShopMenuScore:Double): Unit ={
    val u=new UserBehavior
    u.id=id
    u.customer=customer
    u.shopMenu=shopMenu
    u.browseShopCount=browseShopCount
    u.userShopMenuScore=userShopMenuScore
  }
}