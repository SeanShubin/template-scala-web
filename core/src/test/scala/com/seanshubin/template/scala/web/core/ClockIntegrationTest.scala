package com.seanshubin.template.scala.web.core

import org.scalatest.FunSuite

class ClockIntegrationTest extends FunSuite {
  test("time moves forward") {
    val clock = new ClockIntegration
    val earlier = clock.zonedDateTimeNow
    Thread.sleep(1)
    val later = clock.zonedDateTimeNow
    assert(!earlier.isAfter(later))
    assert(later.isAfter(earlier))
  }
}
