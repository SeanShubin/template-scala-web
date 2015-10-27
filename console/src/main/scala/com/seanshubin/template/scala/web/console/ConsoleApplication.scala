package com.seanshubin.template.scala.web.console

object ConsoleApplication extends App {
  new TopLevelWiring {
    override def commandLineArguments: Seq[String] = args
  }.topLevelRunner.run()
}
