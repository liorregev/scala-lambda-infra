name := "scala-lambda-infa"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-feature", "-deprecation", "-unchecked", "-explaintypes",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-language:reflectiveCalls", "-language:implicitConversions", "-language:postfixOps", "-language:existentials",
  "-language:higherKinds",
  // http://blog.threatstack.com/useful-scala-compiler-options-part-3-linting
  "-Xcheckinit","-Xfatal-warnings", /*"-Xlog-implicits", */"-Xlint",
  "-Ywarn-dead-code", "-Ywarn-numeric-widen", "-Ywarn-unused"
)

wartremoverErrors ++= Seq(
  Wart.StringPlusAny, Wart.FinalCaseClass, Wart.JavaConversions, Wart.Null, Wart.Product, Wart.Serializable,
  Wart.LeakingSealed, Wart.While, Wart.Return, Wart.ExplicitImplicitTypes, Wart.Enumeration, Wart.FinalVal,
  Wart.TryPartial, Wart.TraversableOps, Wart.OptionPartial, ContribWart.SomeApply
)

wartremoverWarnings ++= wartremover.Warts.allBut(
  Wart.Nothing, Wart.DefaultArguments, Wart.Throw, Wart.MutableDataStructures, Wart.NonUnitStatements, Wart.Overloading,
  Wart.Option2Iterable, Wart.ImplicitConversion, Wart.ImplicitParameter, Wart.Recursion,
  Wart.Any, Wart.Equals, // Too many warnings because of spark's Row
  Wart.AsInstanceOf, // Too many warnings because of bad DI practices
  Wart.ArrayEquals // Too many warnings because we're using byte arrays in Spark
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

val AwsSdkVersion = "1.11.586"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.typesafe.play" %% "play-json" % "2.8.0",
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)