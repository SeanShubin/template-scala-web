Scala Web Template
===

Skeleton for Scala Web Application

Depends on [http values](https://github.com/SeanShubin/http-values)

Usage assuming port 4000 and javascript overrides at gui/src/main/resources/

- mvn clean install
- java -jar console/target/scala-web-template.jar 4000 gui/src/main/resources/

To build with dependency analysis

- build [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer)
- mvn clean install -Pdependency

Will likely be used by a project generator
