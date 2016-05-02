import sbt._import sbt.Keys._import sbtunidoc.Plugin.UnidocKeys._import MultiJvmKeys._import ProjectSettings._import ProjectDependencies._version in ThisBuild := "0.7"organization in ThisBuild := "com.rbmhtechnology"scalaVersion in ThisBuild := "2.11.7"lazy val root = (project in file("."))  .aggregate(core, crdt, logCassandra, logLeveldb, examples)  .dependsOn(core, logCassandra, logLeveldb)  .settings(name := "eventuate")  .settings(commonSettings: _*)  .settings(documentationSettings: _*)  .settings(unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(examples))  .settings(libraryDependencies ++= Seq(AkkaRemote))  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)lazy val core = (project in file("eventuate-core"))  .settings(name := "eventuate-core")  .settings(commonSettings: _*)  .settings(protocSettings: _*)  .settings(integrationTestSettings: _*)  .settings(libraryDependencies ++= Seq(AkkaRemote, CommonsIo, Java8Compat, Scalaz))  .settings(libraryDependencies ++= Seq(AkkaTestkit % "test,it", AkkaTestkitMultiNode % "test", Javaslang % "test", JunitInterface % "test", Scalatest % "test,it"))  .configs(IntegrationTest, MultiJvm)  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)lazy val logCassandra = (project in file("eventuate-log-cassandra"))  .dependsOn(core % "compile->compile;it->it;multi-jvm->multi-jvm")  .settings(name := "eventuate-log-cassandra")  .settings(commonSettings: _*)  .settings(integrationTestSettings: _*)  .settings(libraryDependencies ++= Seq(AkkaRemote, CassandraDriver))  .settings(libraryDependencies ++= Seq(AkkaTestkit % "test,it", AkkaTestkitMultiNode % "test", Scalatest % "test,it"))  .settings(libraryDependencies ++= Seq(CassandraUnit % "test,it" excludeAll ExclusionRule(organization = "ch.qos.logback")))  .settings(jvmOptions in MultiJvm += "-Dmultinode.server-port=4712")  .configs(IntegrationTest, MultiJvm)  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)lazy val logLeveldb = (project in file("eventuate-log-leveldb"))  .dependsOn(core % "compile->compile;it->it;multi-jvm->multi-jvm")  .settings(name := "eventuate-log-leveldb")  .settings(commonSettings: _*)  .settings(integrationTestSettings: _*)  .settings(libraryDependencies ++= Seq(AkkaRemote, Leveldb))  .settings(libraryDependencies ++= Seq(AkkaTestkit % "test,it", AkkaTestkitMultiNode % "test", Scalatest % "test,it"))  .settings(jvmOptions in MultiJvm += "-Dmultinode.server-port=4713")  .configs(IntegrationTest, MultiJvm)  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)lazy val crdt = (project in file("eventuate-crdt"))  .dependsOn(core % "compile->compile;it->it;multi-jvm->multi-jvm")  .dependsOn(logLeveldb % "test;it->it;multi-jvm->multi-jvm")  .settings(name := "eventuate-crdt")  .settings(commonSettings: _*)  .settings(protocSettings: _*)  .settings(integrationTestSettings: _*)  .settings(libraryDependencies ++= Seq(AkkaRemote))  .settings(libraryDependencies ++= Seq(AkkaTestkit % "test,it", AkkaTestkitMultiNode % "test", Scalatest % "test,it"))  .settings(jvmOptions in MultiJvm += "-Dmultinode.server-port=4714")  .configs(IntegrationTest, MultiJvm)  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)lazy val examples = (project in file("eventuate-examples"))  .dependsOn(core, logLeveldb)  .settings(name := "eventuate-examples")  .settings(commonSettings: _*)  .settings(exampleSettings: _*)  .settings(libraryDependencies ++= Seq(AkkaRemote, CassandraDriver, Javaslang))  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)