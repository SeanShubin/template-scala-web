package com.seanshubin.template.scala.web.core

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.{DefaultDevonMarshaller, DevonMarshaller}
import com.seanshubin.utility.file_system.FileSystemIntegration
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ConfigurationFactoryImplTest extends FunSuite with EasyMockSugar {
  test("complete configuration") {
    new Helper {
      override def content =
        """{
          |  port 4000
          |  servePathOverride gui/src/main/resources/
          |  optionalPathPrefix /template
          |}
          | """.stripMargin

      override def expected = Right(Configuration(4000, Some("gui/src/main/resources/"), Some("/template")))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("configuration omit optionals") {
    new Helper {
      override def content =
        """{
          |  port 5000
          |}
          | """.stripMargin

      override def expected = Right(Configuration(5000, None, None))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("missing configuration file") {
    new Helper {
      override def content =
        """{
          |  servePathOverride gui/src/main/resources/
          |  optionalPathPrefix /template
          |}
          | """.stripMargin

      override def expected = Left(Seq("Configuration file named 'environment.txt' not found"))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(false)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("missing required field") {
    new Helper {
      override def content =
        """{
          |  servePathOverride gui/src/main/resources/
          |  optionalPathPrefix /template
          |}
          | """.stripMargin

      override def expected = Left(Seq(
        "There was a problem reading the configuration file 'environment.txt': Missing value for port of type Int"))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  test("malformed configuration") {
    new Helper {
      override def content = "{"

      override def expected = Left(Seq("There was a problem reading the configuration file 'environment.txt': Could not match 'element', expected one of: map, array, string, null"))

      override def expecting = () => {
        mockFileSystem.exists(configFilePath).andReturn(true)
        mockFileSystem.readAllBytes(configFilePath).andReturn(contentBytes)
      }

      override def whenExecuting = () => {
        val actual = configurationFactory.validate(args)
        assert(actual === expected)
      }
    }
  }

  trait Helper {
    def content: String

    def expected: Either[Seq[String], Configuration]

    val configFileName: String = "environment.txt"
    val args = Seq(configFileName)
    val mockFileSystem: FileSystemIntegration = mock[FileSystemIntegration]
    val devonMarshaller: DevonMarshaller = new DefaultDevonMarshaller
    val charset: Charset = StandardCharsets.UTF_8
    val configurationFactory = new ConfigurationFactoryImpl(mockFileSystem, devonMarshaller, charset)
    val configFilePath = Paths.get(configFileName)
    val contentBytes = content.getBytes(charset)
    val mocks = Array(mockFileSystem)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(mockFileSystem) {
      whenExecuting()
    }
  }

}
