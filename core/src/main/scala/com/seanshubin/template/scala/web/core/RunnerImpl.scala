package com.seanshubin.template.scala.web.core

class RunnerImpl(server: HttpServer) extends Runner {
  override def run(): Unit = {
    server.start()
    server.join()
  }
}
