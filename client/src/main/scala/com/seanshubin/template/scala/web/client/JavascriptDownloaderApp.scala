package com.seanshubin.template.scala.web.client

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.seanshubin.http.values.client.apache.HttpSender
import com.seanshubin.http.values.core.{RequestValue, ResponseValue, Sender}
import com.seanshubin.template.scala.web.core.json.{JsonMarshaller, JsonMarshallerImpl}

object JavascriptDownloaderApp extends App {
  val sources: Seq[String] = Seq(
    "http://requirejs.org/docs/release/2.1.15/comments/require.js",
    "https://raw.githubusercontent.com/requirejs/text/latest/text.js",
    "https://raw.githubusercontent.com/requirejs/domReady/latest/domReady.js",
    "http://code.jquery.com/jquery-2.1.1.js",
    "http://underscorejs.org/underscore.js",
    "http://epeli.github.io/underscore.string/lib/underscore.string.js",
    "http://code.jquery.com/qunit/qunit-1.15.0.js",
    "http://code.jquery.com/qunit/qunit-1.15.0.css",
    "http://sinonjs.org/releases/sinon-1.10.3.js"
  )

  val sender:Sender = new HttpSender()

  def extractName(target:String) = {
    val lastSlash = target.lastIndexOf('/')
    target.substring(lastSlash+1)
  }

  val downloadDir = Paths.get("gui", "src", "main", "resources", "serve-from-classpath", "lib")
  Files.createDirectories(downloadDir)

  def downloadSource(source:String): (String, JsonFileMetadata) = {
    val request = RequestValue(source, "get", Seq(), Map())
    val response = sender.send(request)
    val name = extractName(source)
    val path = downloadDir.resolve(name)
    val timestamp = JsonTimestamp(System.currentTimeMillis())
    val metadata = if(Files.exists(path)) {
      val existingBytes = Files.readAllBytes(path).toSeq
      if(!ResponseValue.isSuccess(response.statusCode)) {
        JsonFileMetadata(name, source, timestamp, "origin missing")
      } else if(existingBytes == response.body) {
        JsonFileMetadata(name, source, timestamp, "up to date")
      } else {
        JsonFileMetadata(name, source, timestamp, "out of date")
      }
    } else {
      Files.write(path, response.body.toArray)
      JsonFileMetadata(name, source, timestamp, "up to date")
    }
    (name, metadata)
  }

  val files = sources.map(downloadSource).toMap
  val lastChecked = JsonTimestamp(System.currentTimeMillis())
  val metadata =  JsonMetadata(lastChecked, files)
  val jsonMarshaller:JsonMarshaller = new JsonMarshallerImpl
  val metaDataJson = jsonMarshaller.toJson(metadata)
  val metadataPath = downloadDir.resolve("metadata.json")
  val charset = StandardCharsets.UTF_8

  Files.write(metadataPath, metaDataJson.getBytes(charset))
}
