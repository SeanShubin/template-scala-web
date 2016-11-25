package com.seanshubin.template.scala.web.server

import com.seanshubin.http.values.domain.Receiver
import com.seanshubin.http.values.server.jetty.ReceiverToJettyHandler
import com.seanshubin.template.scala.web.domain.HttpServer
import org.eclipse.jetty.server.Server

class JettyHttpServer(port: Int, receiver: Receiver) extends HttpServer {
  private val server: Server = new Server(port)
  server.setHandler(new ReceiverToJettyHandler(receiver))

  override def start(): Unit = {
    server.start()
  }

  override def stop(): Unit = {
    server.stop()
  }

  override def join(): Unit = {
    server.join()
  }
}
