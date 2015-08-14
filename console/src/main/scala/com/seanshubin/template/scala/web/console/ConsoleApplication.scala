package com.seanshubin.template.scala.web.console

object ConsoleApplication extends App with LaunchLifecycleWiring {
  override def commandLineArguments: Seq[String] = args

  launcher.launch()
}
