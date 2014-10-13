package com.seanshubin.template.scala.web.client

import org.joda.time.DateTime

case class JsonTimestamp(timestamp:Long, timestampInLocalTime:String)

object JsonTimestamp{
  def apply(now:Long) = new JsonTimestamp(now, new DateTime(now).toString)
}
