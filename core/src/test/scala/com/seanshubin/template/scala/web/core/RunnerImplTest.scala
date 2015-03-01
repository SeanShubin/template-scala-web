package com.seanshubin.template.scala.web.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class RunnerImplTest extends FunSuite with EasyMockSugar {
  test("start server") {
    val server = mock[HttpServer]
    val runner = new RunnerImpl(server)
    expecting {
      server.start()
      server.join()
    }
    whenExecuting(server) {
      runner.run()
    }
  }
}
