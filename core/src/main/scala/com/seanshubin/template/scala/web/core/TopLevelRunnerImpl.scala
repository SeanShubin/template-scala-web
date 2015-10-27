package com.seanshubin.template.scala.web.core

class TopLevelRunnerImpl(args: Seq[String],
                         configurationFactory: ConfigurationFactory,
                         createRunner: Configuration => Runnable,
                         notifications: Notifications) extends Runnable {
  override def run(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => notifications.configurationError(error)
      case Right(configuration) =>
        notifications.effectiveConfiguration(configuration)
        createRunner(configuration).run()
    }
  }
}
