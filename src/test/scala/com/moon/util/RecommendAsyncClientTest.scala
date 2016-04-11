package com.moon.util

import org.junit.Test

/**
  * Created by lin on 3/19/16.
  */
class RecommendAsyncClientTest {
  @Test
  def testExecuteRequest(): Unit ={
    val response=RecommendAsyncClient.executeRequest("http://localhost:8070/web/common/recommend/recommend-to-customer.action?isRecommend=true")
    if(response.hasResponseBody)
      println(response.getResponseBody())
  }
}
