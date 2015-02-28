package com.seanshubin.template.scala.web.core

class ConfigurationFactoryImpl extends ConfigurationFactory {
  override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
    if (args.length == 1 || args.length == 2) {
      val portAsString = args(0)
      try {
        val port = portAsString.toInt
        val maybeOverridePath = if (args.length == 2) Some(args(1)) else None
        Right(Configuration(port, maybeOverridePath))
      } catch {
        case ex: NumberFormatException => Left(Seq(s"port must be a number, got '$portAsString' instead"))
      }
    } else {
      Left(Seq(
        "expected one or two arguments",
        "(1) the required port number",
        "(2) the optional path override for serving from classpath"))
    }
  }
}
