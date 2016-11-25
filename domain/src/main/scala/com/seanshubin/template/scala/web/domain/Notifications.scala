package com.seanshubin.template.scala.web.domain

import com.seanshubin.http.values.domain.{RequestValue, ResponseValue}

trait Notifications {
  def request(request: RequestValue)

  def response(request: RequestValue, response: ResponseValue)

  def exception(runtimeException: RuntimeException)

  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])
}
