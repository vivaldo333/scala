package net.study.functional.hw

object HomeWork1 extends App {

  // https://www.educba.com/scala-yield/
  /*yield advantages are:
  - yield is used with for loop, for each, map, etc
  - It will not modify the original collection
  - It will always return than the same type of collection on which we are operating
  - At the end, we can easily modify the collection elements without the use of any algorithm.*/

  //yield - example 1
  val x = for (i <- 1 to 5) yield println(i * 2)
  println("end yield - example 1")

  //yield - example 2
  val fruits = List("apple", "banana", "lime", "orange")
  val fruitLengths = for {
    f <- fruits
    if f.length > 4
  } yield println(f.length)
  println("end yield - example 2")

  //yield - example 3
  val arr = Array(10, 50, 40, 60, 80, 90)
  var result = for (x <- arr) yield x
  println("array: " + result)
  // printing output
  for(r <-result)
  {
    println("yield value is ::")
    println(r)
  }
  println("end yield - example 3")
  //------------------------------------------------------
  //---PRELIMINARIES--------------------------------------
  //---Comments
  // a single line comment

  /*
   * a multiline comment
   */

  /**
   * also a multiline comment
   */

  //---Naming conventions
  //Class names: Person, StoreEmployee
  //Variable names: name, firstName
  //Method names: convertToInt, toUpper

  //---SCALA FEATURES----------------------------------
  //---HELLO, WORLD------------------------------------
  //---Install Scala on your computer
  //https://docs.scala-lang.org/getting-started/index.html

  //---TWO TYPES OF VARIABLES
  val s0 = "hello" // immutable
  var i0 = 42 // mutable

  //val p = new Person("Joel Fleischman")
  val s1: String = "hello"
  var i1: Int = 42
  //val p = new Person("Candy") // preferred
  //val p: Person = new Person("Candy") // unnecessarily verbose

  //---A FEW BUILT-IN TYPES
  val b: Byte = 1
  val xx: Int = 1
  val l: Long = 1
  val ss: Short = 1
  val d: Double = 2.0
  val ff: Float = 3.0f

  val i = 123 // defaults to Int
  val x1 = 1.0 // defaults to Double

  var b1 = BigInt(1234567890)
  var b2 = BigDecimal(123456.789)

  val name: String = "Bill"
  val c: Char = 'a'

  //---TWO NOTES ABOUT STRINGS
  val firstName = "John"
  val mi = 'C'
  val lastName = "Doe"
  val name1 = firstName + " " + mi + " " + lastName
  val name2 = s"$firstName $mi $lastName"
  println(s"Name: $firstName $mi $lastName ${1+1}")

  val speech = """Four score and
                 seven years ago
                 our fathers ..."""

  //---CONTROL STRUCTURES
    //basic control structures:
      //if / then / else
      //for loops
      //try / catch / finally
    //unique constructs
      //match expressions
      //for expressions

  //---THE IF/THEN/ELSE CONSTRUCT
  //if (a == b) doSomething()
  /*if (test1) {
    doX()
  } else if (test2) {
    doY()
  } else {
    doZ()
  }*/

  //if expressions always return a result
  //val minValue = if (a < b) a else b
  //val minValue = if (a < b) a else b

  //Conversely, lines of code that don’t return values are called statements, and they are used for their side-effects.

  //---FOR LOOPS
  val nums = Seq(1,2,3)
  for (n <- nums) println(n)

  val people = List(
    "Bill",
    "Candy",
    "Karen",
    "Leo",
    "Regina"
  )
  people.foreach(println)

  val ratings = Map(
    "Lady in the Water" -> 3.0,
    "Snakes on a Plane" -> 4.0,
    "You, Me and Dupree" -> 3.5
  )
  for ((name,rating) <- ratings) println(s"Movie: $name, Rating: $rating")
  ratings.foreach {
    case(movie, rating) => println(s"key: $movie, value: $rating")
  }

}
