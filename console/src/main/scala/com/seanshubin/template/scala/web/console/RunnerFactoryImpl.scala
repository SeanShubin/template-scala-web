package com.seanshubin.template.scala.web.console

import com.seanshubin.template.scala.web.core.{Configuration, Runner, RunnerFactory}

class RunnerFactoryImpl extends RunnerFactory {
  override def createRunner(theConfiguration: Configuration): Runner = {
    new RuntimeLifecycleWiring {
      override def configuration: Configuration = theConfiguration
    }.runner
  }
}
