package com.seanshubin.template.scala.web.core

import org.scalatest.FunSuite

class ClockIntegrationTest extends FunSuite {
  test("time moves forward") {
    val clock = new ClockIntegration
    val now = clock.zonedDateTimeNow
    val later = clock.zonedDateTimeNow
    assert(!now.isAfter(later))
    assert(later.isAfter(now))
  }
}
