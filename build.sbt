name := "akka-http-graphql"
version := "0.0.1-SNAPSHOT"

description := "An example GraphQL server written with akka-http and sangria."

scalaVersion := "2.11.8"
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "1.0.0-RC2",
  "org.sangria-graphql" %% "sangria-spray-json" % "0.3.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.11",
  "com.typesafe.akka" %% "akka-stream" % "2.4.11",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.11",
  "org.sangria-graphql" %% "sangria-akka-streams" % "0.1.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
