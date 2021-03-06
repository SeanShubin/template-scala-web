package com.seanshubin.template.scala.web.console

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.http.values.domain._
import com.seanshubin.template.scala.web.domain._
import com.seanshubin.template.scala.web.server.JettyHttpServer

trait AfterConfigurationWiring {
  def configuration: Configuration

  lazy val emitLine: String => Unit = println
  lazy val classLoaderPrefix: String = "serve-from-classpath"
  lazy val redirects: Map[String, String] = Map("/" -> "/index.html")
  lazy val charset: String = "utf-8"
  lazy val contentByExtension: Map[String, ContentType] = Map(
    ".js" -> ContentType("text/javascript", Some(charset)),
    ".css" -> ContentType("text/css", Some(charset)),
    ".html" -> ContentType("text/html", Some(charset)),
    ".ico" -> ContentType("image/x-icon", None)
  )
  lazy val redirectFunction: String => Option[String] = redirects.get
  lazy val redirectReceiver: Receiver = new RedirectReceiver(redirectFunction)
  lazy val classLoader: ClassLoader = this.getClass.getClassLoader
  lazy val classLoaderReceiver: Receiver = new ClassLoaderReceiver(
    classLoader, classLoaderPrefix, contentByExtension, configuration.optionalServePathOverride)
  lazy val echoReceiver: Receiver = new EchoReceiver()
  lazy val redirectRoute: Route = new RedirectRoute("redirect", redirectReceiver, redirectFunction)
  lazy val classLoaderRoute: Route = new ClassLoaderRoute(
    "class-loader", classLoaderReceiver, contentByExtension)
  lazy val routes: Seq[Route] = Seq(redirectRoute, classLoaderRoute)
  lazy val dispatcher: Receiver = new DispatchingReceiver(routes)
  lazy val clock: Clock = new ClockIntegration()
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val notifications: Notifications = new LineEmittingNotifications(clock, devonMarshaller, emitLine)
  lazy val prefixReceiver: Receiver = PrefixReceiver(configuration.optionalPathPrefix, dispatcher)
  lazy val receiver: Receiver = new FallbackReceiver(
    prefixReceiver, echoReceiver, notifications.request, notifications.response, notifications.exception)
  lazy val server: HttpServer = new JettyHttpServer(configuration.port.get, receiver)
  lazy val afterConfigurationRunner: Runnable = new AfterConfigurationRunnerImpl(server)
}
