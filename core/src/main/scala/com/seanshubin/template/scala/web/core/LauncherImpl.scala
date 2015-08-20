package com.seanshubin.template.scala.web.core

class LauncherImpl(args: Seq[String],
                   configurationFactory: ConfigurationFactory,
                   createRunner: Configuration => Runner,
                   notifications: Notifications) extends Launcher {
  override def launch(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => notifications.configurationError(error)
      case Right(configuration) =>
        notifications.effectiveConfiguration(configuration)
        createRunner(configuration).run()
    }
  }
}
