package com.seanshubin.template.scala.web.core

class LauncherImpl(args: Seq[String],
                   configurationFactory: ConfigurationFactory,
                   runnerFactory: RunnerFactory,
                   notifications: Notifications) extends Launcher {
  override def launch(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => notifications.configurationError(error)
      case Right(configuration) => runnerFactory.createRunner(configuration).run()
    }
  }
}
