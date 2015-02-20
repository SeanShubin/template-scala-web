package com.seanshubin.template.scala.web.core

import com.seanshubin.http.values.core.ContentType

class ConfigurationFactoryImpl extends ConfigurationFactory {
  override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
    if (args.length == 1 || args.length == 2) {
      val portAsString = args(0)
      try {
        val port = portAsString.toInt
        val maybeOverridePath = if (args.length == 2) Some(args(1)) else None
        val classLoaderPrefix = "serve-from-classpath"
        val redirects = Map("/" -> "/index.html")
        val charset = "utf-8"
        val contentByExtension = Map(
          ".js" -> ContentType("text/javascript", Some(charset)),
          ".css" -> ContentType("text/css", Some(charset)),
          ".html" -> ContentType("text/html", Some(charset)),
          ".ico" -> ContentType("image/x-icon", None)
        )
        Right(Configuration(port, classLoaderPrefix, maybeOverridePath, contentByExtension, redirects))
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
