package com.seanshubin.template.scala.web.core

class AfterConfigurationRunnerImpl(server: HttpServer) extends AfterConfigurationRunner {
  override def apply(): Unit = {
    server.start()
    server.join()
  }
}
