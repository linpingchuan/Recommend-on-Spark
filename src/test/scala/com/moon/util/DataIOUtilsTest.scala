package com.moon.util

import org.junit.Test

/**
  * Created by lin on 3/19/16.
  */
class DataIOUtilsTest {
  @Test
  def testWrite(): Unit ={
    DataIOUtils.write("/home/lin/data/spark-data/spark-data.txt","spark data again....")
    DataIOUtils.write("/home/lin/data/spark-data/spark-data.txt","spark data again....")
  }

}
