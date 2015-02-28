package com.seanshubin.template.scala.web.core

import java.nio.charset.Charset
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DevonMarshaller
import com.seanshubin.utility.file_system.FileSystemIntegration

class ConfigurationFactoryImpl(fileSystem: FileSystemIntegration,
                               devonMarshaller: DevonMarshaller,
                               charset: Charset,
                               notifications: Notifications) extends ConfigurationFactory {
  val sampleConfiguration: Configuration = Configuration(
    port = 4000,
    servePathOverride = Some("gui/src/main/resources/"),
    optionalPathPrefix = Some("/template")
  )

  override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
    if (args.length == 1) {
      val configFilePath = Paths.get(args(0))
      try {
        if (fileSystem.exists(configFilePath)) {
          val bytes = fileSystem.readAllBytes(configFilePath)
          val text = new String(bytes, charset)
          val devon = devonMarshaller.fromString(text)
          val config = devonMarshaller.toValue(devon, classOf[Configuration])
          notifications.effectiveConfiguration(config)
          Right(config)
        } else {
          Left(Seq(s"Configuration file named '$configFilePath' not found"))
        }
      } catch {
        case ex: Throwable =>
          Left(Seq(s"There was a problem reading the configuration file '$configFilePath': ${ex.getMessage}"))
      }
    } else {
      val sampleConfigDevon = devonMarshaller.fromValue(sampleConfiguration)
      val prettySampleLines = devonMarshaller.toPretty(sampleConfigDevon)
      Left(Seq(
        "Expected exactly one argument, the name of the configuration file",
        "A typical configuration file might look something like this",
        "") ++ prettySampleLines)
    }
  }
}
