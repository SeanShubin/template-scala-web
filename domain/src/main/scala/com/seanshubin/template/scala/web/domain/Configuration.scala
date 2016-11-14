package com.seanshubin.template.scala.web.domain

case class Configuration(port: Option[Int],
                         optionalServePathOverride: Option[String],
                         optionalPathPrefix: Option[String]) {
  def replaceNullsWithDefaults(): Configuration = {
    val newPort = port match {
      case Some(_) => port
      case None => Configuration.Default.port
    }
    val newServePathOverride = optionalServePathOverride match {
      case Some(_) => optionalServePathOverride
      case None => Configuration.Default.optionalServePathOverride
    }
    val newPathPrefix = optionalPathPrefix match {
      case Some(_) => optionalPathPrefix
      case None => Configuration.Default.optionalPathPrefix
    }
    Configuration(
      port = newPort,
      optionalServePathOverride = newServePathOverride,
      optionalPathPrefix = newPathPrefix)
  }
}

object Configuration {
  val Sample: Configuration = Configuration(
    port = Some(4000),
    optionalServePathOverride = Some("gui/src/main/resources/"),
    optionalPathPrefix = Some("/template")
  )
  val Default: Configuration = Configuration(
    port = Some(80),
    optionalServePathOverride = None,
    optionalPathPrefix = None
  )
}
