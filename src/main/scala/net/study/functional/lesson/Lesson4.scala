package net.study.functional.lesson


import java.io.{Closeable, File}
import scala.io.{BufferedSource, Source}
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.util.control.Exception.{Catch, allCatch, catching}

object Lesson4 extends App {
  //All exception are unchecked

  //handle exceptions
  //1) try - catch - finally
  //1.1) Using () - try with resources - from 2.13
  //2) Monades - Try, Option, Either
  //3) Catcher object

  case class MyCustomException(message: String) extends RuntimeException
  case class PriorityException(message: String) extends RuntimeException
  case class MyAnotherCustomException(message: String) extends RuntimeException


  @throws[RuntimeException]
  def riskyMethod(isRisky: Boolean): String = {
    println("riskyMethod")
    if (isRisky) throw MyCustomException("MyCustomException")
    "Answer"
  }

  //annotation @throw need for compiler
  @throws[RuntimeException]
  def anotherRiskyMethod(isRisky: Boolean): String = {
    println("anotherRiskyMethod")
    if (isRisky) throw MyAnotherCustomException("MyAnotherCustomException")
    "Answer"
  }

  //1) try - catch - finally

  //return Any (parent for String and Unit
  val tryResult: Any = try {
    //return String
    riskyMethod(false)
  } catch {
    //return Unit
    case PriorityException(innerMessage) => println(innerMessage)
    case ex@(_: MyCustomException | _: MyAnotherCustomException) => println(ex)
    case a: Throwable => println(a)
  } finally {
    "Finally answer"
  }

  //java - return "Finally answer"
  //scala - return "Answer" (finally was run, but return was ignored
  //scala - finally is used for closing resources
  println(tryResult)

  //2) Try monad
  //Try/Either flatMap -> should be converted to Option then flatMap
  val x: Try[(String, String)] = for {
    r1 <- Try(riskyMethod(false))
    //if r1 would be exception then r2 wouldn't be run
    r2 <- Try(anotherRiskyMethod(false))
  //returns Tuple2
  } yield (r1, r2)

  println(x.isFailure, x.isSuccess)

  x match {
    case Success(calculated) => println(calculated)
    case Failure(ex) => println(ex)
  }

  //Catch object - proxy object that builds monad
  //could throw an exception

  //could handled all
  val commonCatcher: Catch[Nothing] = allCatch

  val specialCatcher: Catch[Nothing] = catching(classOf[MyCustomException])

  val resultTry: Try[String] = allCatch withTry riskyMethod(true)
  val resultOpt: Option[String] = allCatch opt riskyMethod(true)
  //left - exception, right - success
  val resultEither: Either[Throwable, String] = allCatch either riskyMethod(true)

  println(resultTry)
  println(resultOpt)
  println(resultEither)

  println(resultEither.left.map(ex => "MyException"))

  //catches only MyCustomException
  val specialCatcherResult: Try[String] = specialCatcher withTry riskyMethod(true)
  //will be MyAnotherCustomException
  //val specialCatcherResult2: Try[String] = specialCatcher withTry anotherRiskyMethod(true)

  //"<:" - R extends Closeable in java
  // R <: Closeable - контрваріантність
  //(closeable: R)(body: R => A) - controle structure - ділення контексту на 2 контекста: input params and body
    //можна було все написати як вхідні параметри
  def usingSource[A, R <: Closeable](closeable: R)(body: R => A): A =
    try body(closeable) finally closeable.close()

  def getFile(source: String): BufferedSource = Source.fromFile(source)

  val sourceBusinessLogic = (source: BufferedSource) => source.getLines().mkString("")

  val result2: String = usingSource(getFile("")) {sourceBusinessLogic}

  //Either доповнює Try
  //Left - terminal operation
  //by default monad works with Right
  // Either map - працює від правої частини
  // left right - method for directly set what side use for next operations
  val eitherRight: Right[Nothing, Int] = Right(1)
  //method that returns Nothing
  ???
  val eitherLeft: Either[String, Long] = Left("some string")

  val anotherRight: Either[String, Int] = Right(4)

  val eitherComputeResult = (for {
    r1 <- eitherRight
    //r2 <- eitherLeft.left.map(_ => 10)
    r2 <- (eitherLeft swap) map (_ => 100)
    //r2 <- (eitherLeft swap) left map (_ => 100)
    //r2 <- eitherLeft
    r3 <- anotherRight
  } yield r1 + r2 + r3) match {
    //case Left(_) => 0
    case Left(x) => x
    case Right(intValue) => intValue
  }

}

