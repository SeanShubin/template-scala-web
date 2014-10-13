package com.seanshubin.template.scala.web.core

class LauncherImpl(args: Seq[String],
                   configurationFactory: ConfigurationFactory,
                   errorHandler: ErrorHandler,
                   runnerFactory: RunnerFactory) extends Launcher {
  override def launch(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => errorHandler.handleConfigurationError(error)
      case Right(configuration) => runnerFactory.createRunner(configuration).run()
    }
  }
}
