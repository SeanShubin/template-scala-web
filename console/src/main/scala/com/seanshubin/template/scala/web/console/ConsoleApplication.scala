package com.seanshubin.template.scala.web.console

object ConsoleApplication extends App with LauncherWiring {
  override def commandLineArguments: Seq[String] = args

  launcher.launch()
}
