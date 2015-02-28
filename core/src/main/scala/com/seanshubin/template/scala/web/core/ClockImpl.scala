package com.seanshubin.template.scala.web.core

import java.time.ZonedDateTime

class ClockImpl extends Clock {
  override def zonedDateTimeNow: ZonedDateTime = ZonedDateTime.now()
}
