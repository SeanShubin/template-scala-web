package com.seanshubin.template.scala.web.core

import java.io.{PrintWriter, StringWriter}

import com.seanshubin.devon.core.devon.DevonMarshaller
import com.seanshubin.http.values.core.{RequestValue, ResponseValue}

class LineEmittingNotifications(clock: Clock, devonMarshaller: DevonMarshaller, emit: String => Unit) extends Notifications {
  private val lock = new Object()

  override def request(request: RequestValue): Unit = {
    lock.synchronized {
      wrapLines("request", request.toMultipleLineString).foreach(emit)
    }
  }

  override def response(request: RequestValue, response: ResponseValue): Unit = {
    lock.synchronized {
      wrapLines("response", request.toMultipleLineString ++ response.toMultipleLineString).foreach(emit)
    }
  }

  override def exception(runtimeException: RuntimeException): Unit = {
    lock.synchronized {
      wrapLines("exception", exceptionLines(runtimeException)).foreach(emit)
    }
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    val devon = devonMarshaller.fromValue(configuration)
    val pretty = devonMarshaller.toPretty(devon)
    emit("Effective configuration:")
    pretty.foreach(emit)
  }

  def exceptionLines(ex: Throwable): Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split( """\r\n|\r|\n""").toSeq
    lines
  }

  def wrapLines(caption: String, lines: Seq[String]): Seq[String] = {
    val now = clock.zonedDateTimeNow()
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
