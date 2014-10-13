package com.seanshubin.template.scala.web.server

import com.seanshubin.http.jetty_server.ReceiverToJettyHandler
import com.seanshubin.http.values.Receiver
import com.seanshubin.template.scala.web.core.HttpServer
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
