package com.seanshubin.template.scala.web.core

trait RunnerFactory {
  def createRunner(configuration: Configuration): Runner
}
