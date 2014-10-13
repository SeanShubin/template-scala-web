package com.seanshubin.template.scala.web.console

import com.seanshubin.template.scala.web.core.{RunnerFactory, Runner, Configuration}

class RunnerFactoryImpl extends RunnerFactory {
  override def createRunner(theConfiguration: Configuration): Runner = {
    new RunnerWiring {
      override def configuration: Configuration = theConfiguration
    }.runner
  }
}
