package com.seanshubin.template.scala.web.core

case class Configuration(port: Int,
                         servePathOverride: Option[String],
                         optionalPathPrefix: Option[String])

object Configuration {
  val Sample: Configuration = Configuration(
    port = 4000,
    servePathOverride = Some("gui/src/main/resources/"),
    optionalPathPrefix = Some("/template")
  )
}
