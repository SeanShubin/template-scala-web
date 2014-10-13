package com.seanshubin.template.scala.web.core

import com.seanshubin.http.values.core.{RequestValue, ResponseValue}

trait Notifications {
  def request(request:RequestValue)
  def response(request:RequestValue, response:ResponseValue)
  def exception(runtimeException:RuntimeException)
}
