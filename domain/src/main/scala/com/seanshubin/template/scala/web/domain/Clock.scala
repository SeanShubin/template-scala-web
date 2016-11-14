package com.seanshubin.template.scala.web.domain

import java.time.ZonedDateTime

trait Clock {
  def zonedDateTimeNow(): ZonedDateTime
}
