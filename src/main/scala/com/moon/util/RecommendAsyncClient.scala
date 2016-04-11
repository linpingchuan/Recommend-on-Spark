package com.moon.util

import com.ning.http.client.{Response, AsyncHttpClientConfig, AsyncHttpClient}
import org.slf4j.LoggerFactory

/**
  * Created by lin on 3/19/16.
  */
object RecommendAsyncClient {
  def configClient():AsyncHttpClient={
    val builder = new AsyncHttpClientConfig.Builder()
    builder.setCompressionEnforced(true)
      .setAllowPoolingConnections(true)
      .setRequestTimeout(50000)
      .build()
    new AsyncHttpClient(builder.build())
  }

  def executeRequest(path:String):Response={
    configClient.prepareGet(path).execute().get()
  }
}
