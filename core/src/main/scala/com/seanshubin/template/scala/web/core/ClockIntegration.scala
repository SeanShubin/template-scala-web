package com.seanshubin.template.scala.web.core

import java.time.ZonedDateTime

class ClockIntegration extends Clock {
  override def zonedDateTimeNow: ZonedDateTime = ZonedDateTime.now()
}
