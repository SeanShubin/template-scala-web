package com.seanshubin.template.scala.web.domain

trait HttpServer {
  def start()

  def stop()

  def join()
}
