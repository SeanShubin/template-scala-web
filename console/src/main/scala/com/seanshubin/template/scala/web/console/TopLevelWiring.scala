package com.seanshubin.template.scala.web.console

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.template.scala.web.core._
import com.seanshubin.utility.filesystem.{FileSystemIntegration, FileSystemIntegrationImpl}

trait TopLevelWiring {
  def commandLineArguments: Seq[String]

  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val clock: Clock = new ClockIntegration
  lazy val notifications: Notifications = new LineEmittingNotifications(clock, devonMarshaller, emitLine)
  lazy val configurationFactory: ConfigurationFactory = new ConfigurationFactoryImpl(
    fileSystem, devonMarshaller, charset)
  lazy val createAfterConfigurationRunner: Configuration => AfterConfigurationRunner = (theConfiguration) => new AfterConfigurationWiring {
    override def configuration: Configuration = theConfiguration
  }.afterConfigurationRunner
  lazy val topLevelRunner: TopLevelRunner = new TopLevelRunnerImpl(
    commandLineArguments, configurationFactory, createAfterConfigurationRunner, notifications)
}
