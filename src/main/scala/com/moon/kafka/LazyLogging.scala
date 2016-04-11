package com.moon.kafka

import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by lin on 4/10/16.
  */
trait LazyLogging {
  protected lazy val logger:Logger=LoggerFactory.getLogger(getClass.getName)
}

trait StrictLogging{
  protected val logger:Logger=LoggerFactory.getLogger(getClass.getName)
}