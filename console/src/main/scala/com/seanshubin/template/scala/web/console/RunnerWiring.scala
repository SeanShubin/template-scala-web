package com.seanshubin.template.scala.web.console

import com.seanshubin.http.values._
import com.seanshubin.template.scala.web.core._
import com.seanshubin.template.scala.web.server.JettyHttpServer

trait RunnerWiring {
  def configuration: Configuration

  lazy val redirectReceiver: Receiver = new RedirectReceiver(configuration.redirectFunction)
  lazy val classLoader: ClassLoader = this.getClass.getClassLoader
  lazy val classLoaderReceiver: Receiver = new ClassLoaderReceiver(
    classLoader, configuration.classLoaderPrefix, configuration.contentByExtension, configuration.overridePath)
  lazy val echoReceiver: Receiver = new EchoReceiver()
  lazy val redirectGate:Gate = new RedirectGate("redirect", redirectReceiver, configuration.redirectFunction)
  lazy val classLoaderGate:Gate = new ClassLoaderGate(
    "class-loader", classLoaderReceiver, configuration.contentByExtension)
  lazy val gates:Seq[Gate] = Seq(redirectGate, classLoaderGate)
  lazy val dispatcher:Receiver = new DispatchingReceiver(gates)
  lazy val clock:Clock = new ClockImpl()
  lazy val notifications:Notifications = new NotificationsImpl(clock)
  lazy val receiver: Receiver = new FallbackReceiver(
    dispatcher, echoReceiver, notifications.request, notifications.response, notifications.exception)
  lazy val server: HttpServer = new JettyHttpServer(configuration.port, receiver)
  lazy val runner: Runner = new RunnerImpl(server)
}
