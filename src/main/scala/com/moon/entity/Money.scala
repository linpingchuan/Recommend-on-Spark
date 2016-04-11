package com.moon.entity

/**
  * Created by lin on 3/8/16.
  */
class Money extends Serializable{
  var id : Long = _
  var sum : Double = _
}

object Money{
  def apply(id:Long,sum:Double): Unit ={
    val m=new Money
    m.id=id
    m.sum=sum
  }
}
