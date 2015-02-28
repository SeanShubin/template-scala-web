package com.seanshubin.template.scala.web.core

import java.time.ZonedDateTime

trait Clock {
  def zonedDateTimeNow(): ZonedDateTime
}
