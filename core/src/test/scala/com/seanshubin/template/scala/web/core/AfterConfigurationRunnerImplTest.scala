package com.seanshubin.template.scala.web.core

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class AfterConfigurationRunnerImplTest extends FunSuite {
  test("don't start server upon construction") {
    val server = new FakeHttpServer()
    new AfterConfigurationRunnerImpl(server)
    assert(server.sideEffects === Seq())
  }

  test("start server when run called") {
    val server = new FakeHttpServer()
    val runner = new AfterConfigurationRunnerImpl(server)
    runner.run()
    assert(server.sideEffects === Seq("start", "join"))
  }

  class FakeHttpServer extends HttpServer {
    val sideEffects: ArrayBuffer[String] = new ArrayBuffer()

    override def start(): Unit = sideEffects.append("start")

    override def stop(): Unit = sideEffects.append("stop")

    override def join(): Unit = sideEffects.append("join")
  }

}
