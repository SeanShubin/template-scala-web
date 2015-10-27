package com.seanshubin.template.scala.web.core

import com.seanshubin.http.values.core.{RequestValue, ResponseValue}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class ArgsAfterConfigurationRunnerImplTest extends FunSuite {
  test("valid configuration") {
    val helper = new Helper(validationResult = Right(Configuration.Sample))
    helper.launcher.run()
    assert(helper.sideEffects.size === 2)
    assert(helper.sideEffects(0) ===("notifications.effectiveConfiguration", Configuration.Sample))
    assert(helper.sideEffects(1) ===("runner.run", ()))
  }

  test("invalid configuration") {
    val helper = new Helper(validationResult = Left(Seq("error")))
    helper.launcher.run()
    assert(helper.sideEffects.size === 1)
    assert(helper.sideEffects(0) ===("notifications.configurationError", Seq("error")))
  }

  class Helper(validationResult: Either[Seq[String], Configuration]) {
    val sideEffects: ArrayBuffer[(String, Any)] = new ArrayBuffer()
    val configurationFactory = new FakeConfigurationFactory(Seq("foo.txt"), validationResult)
    val runner = new FakeAfterConfigurationRunner(sideEffects)
    val runnerFactory = (configuration: Configuration) => runner
    val notifications = new FakeNotification(sideEffects)
    val launcher = new TopLevelRunnerImpl(Seq("foo.txt"), configurationFactory, runnerFactory, notifications)
  }

  class FakeConfigurationFactory(expectedArgs: Seq[String], result: Either[Seq[String], Configuration]) extends ConfigurationFactory {
    override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
      assert(args === expectedArgs)
      result
    }
  }

  class FakeNotification(sideEffects: ArrayBuffer[(String, Any)]) extends Notifications {
    def append(name: String, value: Any): Unit = {
      sideEffects.append(("notifications." + name, value))
    }

    override def request(request: RequestValue): Unit = append("request", request)

    override def configurationError(lines: Seq[String]): Unit = append("configurationError", lines)

    override def effectiveConfiguration(configuration: Configuration): Unit = append("effectiveConfiguration", configuration)

    override def response(request: RequestValue, response: ResponseValue): Unit = append("response", (request, response))

    override def exception(runtimeException: RuntimeException): Unit = append("runtimeException", runtimeException)
  }

  class FakeAfterConfigurationRunner(sideEffects: ArrayBuffer[(String, Any)]) extends Runnable {
    override def run(): Unit = sideEffects.append(("runner.run", ()))
  }

}
