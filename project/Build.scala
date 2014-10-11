import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "battlehack"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
     "com.amazonaws" % "aws-java-sdk" % "1.3.11",
     "nl.rhinofly" %% "play-s3" % "3.1.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Rhinofly Internal Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local"
  )

}
