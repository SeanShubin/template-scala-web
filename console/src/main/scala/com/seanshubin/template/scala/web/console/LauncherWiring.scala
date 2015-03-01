package com.seanshubin.template.scala.web.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.core.devon.{DefaultDevonMarshaller, DevonMarshaller}
import com.seanshubin.template.scala.web.core._
import com.seanshubin.utility.file_system.{FileSystemIntegration, FileSystemIntegrationImpl}

trait LauncherWiring {
  def commandLineArguments: Seq[String]

  lazy val emitLine: String => Unit = println
  lazy val errorHandler: ErrorHandler = new ErrorHandlerImpl(emitLine)
  lazy val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val devonMarshaller: DevonMarshaller = new DefaultDevonMarshaller
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val clock: Clock = new ClockIntegration
  lazy val notifications: Notifications = new LineEmittingNotifications(clock, devonMarshaller, emitLine)
  lazy val configurationFactory: ConfigurationFactory = new ConfigurationFactoryImpl(
    fileSystem, devonMarshaller, charset)
  lazy val runnerFactory: RunnerFactory = new RunnerFactoryImpl()
  lazy val launcher: Launcher = new LauncherImpl(
    commandLineArguments, configurationFactory, errorHandler, runnerFactory)
}
