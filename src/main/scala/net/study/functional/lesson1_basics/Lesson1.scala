package net.study.functional.lesson1_basics

import java.util.UUID
import java.util.concurrent.{Executor, Executors, TimeUnit}
import scala.util.Try

//object - 1 instance of class (as singleton) in process
//can be only 1 method in object
//no static in Scala
//"extends App" - for add "main" method
object Lesson1 /*extends App*/ {

  def main(args: Array[String]): Unit = {
    //def
    //var
    //val

    //expression -> return result
    //final variable
    val myString = "name" + 1

    //not final
    var myChangeableValue = "fist value"

    //statement -> procedure that return void (not return any value)

    //method's signature includes:
      //модифікатор доступу
      //return type
      //input parameters
      //throws exceptions

    //only 1 return available in method
    def sum(int: Int, int2: Int) = int + int2

    def sum2(int: Int, int2: Int) = {
      int + int2
    }



    //method vs function
    //method is one-time object
    //function is object till process end

    //someFunc - will be created 1 time during invoking
    //"def sum" -> "int + int2" - will be created every time during invoking
    val someFunc = (int: Int, int2: Int) => int + int2


    //2 рази буде створюватись об"єкт для розрахунку
    println(sum(Int.MaxValue, Int.MaxValue))
    println(sum(Int.MaxValue, Int.MaxValue))
    //1 раз буде створений об"єкт для розрахунку
    println(someFunc(Int.MaxValue, Int.MaxValue))
    println(someFunc(Int.MaxValue, Int.MaxValue))

    val generateUUIDFunc: () => UUID = {
      //"random" - precompiled - static value
      val random = UUID.randomUUID()
      () => random
    }

    //same result, because same object
    println(generateUUIDFunc)
    println(generateUUIDFunc)

    def generateUUID: () => UUID = {
      val random = UUID.randomUUID ()
      () => random
    }

    //different result
    println(generateUUID())
    println(generateUUID())

    //краще з методом працювати
    //метод можна конвертнути в функцію

    //функцію - використовувати як partial function і треба швидше

    //lazy - буде викликана змінна тільки тоді, коли ми вперше звернемося до змінної
    //lazy val - це final в майбутньому (відмічена/assigner ссылка,але не розрахована)
    //lazy - потокобезпечне
    //використовувати, коли:
      //об"являєть коннекшин до БД
      //коли змінна може ініалізуватися пізніше, а викликана раніше (lazy врятує від NPE)
      //lazy loading в окремих threads
    lazy val myLazyString: String = {
      println("I am ready")
      "Hello!"
    }

    /*
    val executor = Executors.newSingleThreadScheduledExecutor()
    executor.schedule(new Runnable {
      override def run(): Unit = println(myLazyString)
    }, 5, TimeUnit.SECONDS)
    */

    //use methods from RichInt class
    val range = 1 to 10
    range.foreach(println(x => println(x)))
    range.foreach(println(_))
    range.foreach(println)
    (1 until  10).foreach(println(_))
    (1 until  10).foldLeft(0)((accumulator, value) => accumulator + value)
    (1 until  10).foldLeft(0)(_ + _)

    //--1-start------------------------
    val postProcessResult: Int => Unit = r => println(r)

    //control structures - reusable
    def calculateAndSend2(int: Int, int2: Int) (postFunc: Int => Unit): Unit = {
      postFunc(int2 + int)
    }

    //стиль написання: калькуляція і відсилання через використання вхідної функції "postProcessResult"
    calculateAndSend2(1, 2) {postProcessResult}
    //--1-end------------------------

    //--2-start------------------------
    def calculateAndSend(int: Int, int2: Int) (postFunc: Int => Unit): Unit = postFunc(int + int2)

    //результат функції, я callback і потім можна обробляти результат
    calculateAndSend(1, 2) {result =>
      println(result)
    }
    //--2-end------------------------

    //розгорнута сігнатура -> декілька обробляємих функцій
    //def func1(param: Int)(func2: Int => Int)(func3: Int => Unit): Unit => {}

    //--3-start------------------------
    implicit class AfterTryResult[V](tryContainer: Try[V]) {
      def andFinally(finallyBlock: => Unit): Try[V] = try tryContainer finally Try(finallyBlock)
    }

    Try(println(_)) andFinally {
      println("Clean Up")
    }
    //--3-end------------------------
  }

  //condition in Scala
    //if-else
    //pattern matching

  //if-else - expression (return value)
  //тернарного оператора и Elvis-оператора - нету в Scala

  val myString1 = ""
  //ідіоматична ініціалізація
  val result: Int = if(myString1.nonEmpty) 1 else 0
  //return Unit
  //val result: AnyVal = if (myString1.nonEmpty) 1 else {}

  //create instance of Unit
  val unit: Unit = ()

  //Compilation error - an expression of type Null is one error found
  //val nullableInt: Int = null
  //set "null" with defined type
  val nullableInt: Int = null.asInstanceOf[Int]
  //Optional
  val myInt: Option[Int] = None
  myInt match {
    case Some(_) => println("Yes")
    case None => println("No")
  }

  //break - not as operator, but as method

  //take while
  //recursion foreach
  //tail recursion -> інтерпритується в for loop (не забиває stack), but recursion - забиває stack
  //switch case -> інтерпритується в if-else
}
