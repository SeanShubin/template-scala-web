package com.seanshubin.template.scala.web.domain

import java.io.{PrintWriter, StringWriter}

import com.seanshubin.devon.domain.DevonMarshaller
import com.seanshubin.http.values.domain.{RequestValue, ResponseValue}

class LineEmittingNotifications(clock: Clock, devonMarshaller: DevonMarshaller, emit: String => Unit) extends Notifications {
  private val lock = new Object()

  override def request(request: RequestValue): Unit = {
    syncEmit(wrapLines("request", request.toMultipleLineString))
  }

  override def response(request: RequestValue, response: ResponseValue): Unit = {
    syncEmit(wrapLines("response", request.toMultipleLineString ++ response.toMultipleLineString))
  }

  override def exception(runtimeException: RuntimeException): Unit = {
    syncEmit(wrapLines("exception", exceptionLines(runtimeException)))
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    val devon = devonMarshaller.fromValue(configuration)
    val pretty = devonMarshaller.toPretty(devon)
    emit("Effective configuration:")
    pretty.foreach(emit)
  }

  override def configurationError(lines: Seq[String]): Unit = {
    lines.foreach(emit)
  }

  private def exceptionLines(ex: Throwable): Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split( """\r\n|\r|\n""").toSeq
    lines
  }

  private def wrapLines(caption: String, lines: Seq[String]): Seq[String] = {
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

  private def syncEmit(lines: Seq[String]): Unit = {
    lock.synchronized {
      lines.foreach(emit)
    }
  }
}
