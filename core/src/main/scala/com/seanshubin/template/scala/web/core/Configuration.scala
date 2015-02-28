package com.seanshubin.template.scala.web.core

case class Configuration(port: Int,
                         servePathOverride: Option[String],
                         optionalPathPrefix: Option[String])
