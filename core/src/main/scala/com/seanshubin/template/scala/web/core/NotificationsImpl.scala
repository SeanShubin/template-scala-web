package com.seanshubin.template.scala.web.core

import java.io.{PrintWriter, StringWriter}

import com.seanshubin.http.values.{RequestValue, ResponseValue}
import org.joda.time.{DateTime, DateTimeZone}

class NotificationsImpl(clock:Clock) extends Notifications {
  private val lock = new Object()
  override def request(request: RequestValue): Unit = {
    lock.synchronized {
      wrapLines("request", request.toMultipleLineString).foreach(println)
    }
  }

  override def response(request:RequestValue, response: ResponseValue): Unit = {
    lock.synchronized {
      wrapLines("response", request.toMultipleLineString ++ response.toMultipleLineString).foreach(println)
    }
  }

  override def exception(runtimeException: RuntimeException): Unit = {
    lock.synchronized {
      wrapLines("exception", exceptionLines(runtimeException)).foreach(println)
    }
  }

  def exceptionLines(ex:Throwable):Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split("""\r\n|\r|\n""").toSeq
    lines
  }

  def wrapLines(caption:String, lines:Seq[String]):Seq[String] = {
    val timeZone = DateTimeZone.forID("UTC")
    val now = new DateTime(clock.currentTimeMillis(), timeZone)
    val timeString = now.toString
    val stars = "*" * 30
    val headerBody = s"$stars $caption ($timeString) $stars"
    val header = s"/$headerBody\\"
    val footer = s"\\$headerBody/"
    val newLines = lines.map(line => "  " + line)
    val wrapped = Seq(header) ++ newLines ++ Seq(footer)
    wrapped
  }
}
