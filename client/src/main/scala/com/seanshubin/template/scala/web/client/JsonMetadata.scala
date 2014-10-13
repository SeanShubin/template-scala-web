package com.seanshubin.template.scala.web.client

case class JsonMetadata(lastChecked:JsonTimestamp, files:Map[String, JsonFileMetadata])
