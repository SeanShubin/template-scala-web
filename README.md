Generated from Scala Web Template
===

Skeleton for Scala Web Application

Template for use with [generate-from-template](https://github.com/SeanShubin/generate-from-template)

Dependencies

- [http values](https://github.com/SeanShubin/http-values)
- [web sync](https://github.com/SeanShubin/web-sync)

Usage assuming port 4000 and javascript overrides at gui/src/main/resources/

- mvn clean install
- java -jar console/target/scala-web-template.jar 4000 gui/src/main/resources/

To build with dependency analysis

- build [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer)
- mvn clean install -Pdependency
