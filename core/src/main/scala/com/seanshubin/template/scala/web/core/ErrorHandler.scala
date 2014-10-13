package com.seanshubin.template.scala.web.core

trait ErrorHandler {
  def handleConfigurationError(lines: Seq[String])
}
