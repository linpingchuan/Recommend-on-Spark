package com.moon.dao.hbase

import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{HTable, HBaseAdmin, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.junit.Test

/**
  * Created by lin on 4/30/16.
  */
class HbaseTest {
  @Test
  def testHbase(): Unit ={
    val conf=HBaseConfiguration.create()
    conf.addResource("hbase-site.xml")
    val table=new HTable(conf,"test")

  }

}
