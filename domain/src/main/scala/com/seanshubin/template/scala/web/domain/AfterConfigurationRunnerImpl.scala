package com.seanshubin.template.scala.web.domain

class AfterConfigurationRunnerImpl(server: HttpServer) extends Runnable {
  override def run(): Unit = {
    server.start()
    server.join()
  }
}
