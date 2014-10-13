package com.seanshubin.template.scala.web.core

import com.seanshubin.http.values.core.ContentType

case class Configuration(port: Int,
                         classLoaderPrefix: String,
                         overridePath: Option[String],
                         contentByExtension: Map[String, ContentType],
                         redirects: Map[String, String]) {
  def redirectFunction(uri: String): Option[String] = {
    redirects.get(uri)
  }
}
