package com.seanshubin.template.scala.web.core

class ErrorHandlerImpl(emitLine: String => Unit) extends ErrorHandler {
  override def handleConfigurationError(lines: Seq[String]): Unit = {
    lines.foreach(emitLine)
  }
}
