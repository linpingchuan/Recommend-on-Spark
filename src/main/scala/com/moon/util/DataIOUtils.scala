package com.moon.util

import java.io.{File, FileOutputStream, ByteArrayOutputStream, RandomAccessFile}
import java.nio.ByteBuffer

/**
  * Created by lin on 3/19/16.
  */
object DataIOUtils {
  def write(path:String,content:String): Unit ={
    val file=new RandomAccessFile(path,"rw")
    val channel=file.getChannel
    try{
      val buf=ByteBuffer.allocate(1024)
      buf.clear()
      buf.put(new StringBuilder(content).append('\n').toString.getBytes())
      buf.flip()
      while(buf.hasRemaining)
        channel.write(buf,channel.size())
    }catch{
      case e:Exception => None
    }finally {
      channel.close()
    }
  }
}
