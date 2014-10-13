package com.seanshubin.template.scala.web.core

class ClockImpl extends Clock {
  override def currentTimeMillis(): Long = System.currentTimeMillis()
}
