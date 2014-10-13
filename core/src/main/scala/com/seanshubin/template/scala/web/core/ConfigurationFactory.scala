package com.seanshubin.template.scala.web.core

trait ConfigurationFactory {
  def validate(args: Seq[String]): Either[Seq[String], Configuration]
}
