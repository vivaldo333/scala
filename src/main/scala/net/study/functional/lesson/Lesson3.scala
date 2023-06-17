package net.study.functional.lesson

object Lesson3 extends App {

  //immutable class - changes in class generate copy of object (guaranty that attributes would be not changable)
    //final class -
    //init attributes in constructor
    //get return attribute copy of attribute (shallow copy)

  //case class-----------------------------------------------------------------------------------
  //name and age are final (val)
  //good practice - add default parameter at the end
  case class Person(name: String, age: Int = 30, isMarried: Boolean = true)

  //apply / unapply - methods for create intance of class in companion object
  var personA = Person("Sasha", 30) //same as var personA = Person.apply("Sasha", 30)
  var personB = Person("Sasha", 30)
  // == is "equals" in scala
  // ==  - in scala is not the same as java "=="
  println(personA == personB)
  println(personA equals  personB)
  //as "==" in java
  println(personA eq personB)
  println(personA eq personA)

  //clone with changing property (others properties will be cloned as shallow copies)
  val personC = personB.copy(age = 22)

  //unapply - destruction (there is no such in java)
  personC match {
    //use "unapply" methods
    case Person(name, age, isMarried) => println(s"name=$name, age=$age")
    case _ =>
  }

  val result = personC match {
    //use "unapply" methods and use "equals" method for check equality of parameters - age.equals(20)
    case Person(name, 20, isMarried) => s"name=$name, age=20"
    case Person("Ivan", age, isMarried) => s"name=Ivan, age=$age"
    //case Person(name, age) => s"name=$name, age=$age"
    //прив"язяти результат до змінної "a", але змінна не використовується
    case a => s"another person = $a"
    //ignore result
    //case _ => "no person"
  }
  println(s"result: $result")

  def analyze(person: Person): String = person match {
    //use "unapply" methods a
    case Person(name, 20, isMarried) => s"name=$name, age=20"
    case Person("Ivan", age, isMarried) => s"name=Ivan, age=$age"
    //scala.MatchError appears if all cases woun't be covered (for example comment "case a ..."
    case a => s"another person = $a"
  }

  //var personD = Person("Oleg", isMarried = false, age = 20)
  var personD = Person(name = "Oleg", isMarried = false)


  println(analyze(personA))
  println(analyze(personB))
  println(analyze(personC))

  //OPTIONS-----------------------------------------------------------------------------------
  //monada - container with same type inside (it is like collection)
  //has 2 methods: flatMap (зробити flat собі подібних), unit (родити саму себе)
  //monada not opens its itself

  //Option
  //has value or not
  //Option - підтип Some, чи None ... описує загальну структуру
  val maybeInt = Option(1)
  val maybeInt1 = Some(1) //final case class
  val maybeInt2 = None //final case class
  val maybeInt3: Option[Int] = Option(1)
  val maybeInt4: Option[Int] = None

  val resultContext = maybeInt3 match {
    //якщо в монаді щось є, то верни результат
    case Some(a) => a
    //якщо нічого не має, то поверни default value "0"
    case None => 0
  }

  println(resultContext)

  val mergeMonad: Option[Int] = maybeInt4 orElse Some(1)

  ( maybeInt4 orElse Some(1)) match {
    case Some(value) => println(s"to do something $value")
    case None => println("to do smth node")
  }

  //maybeInt3.getOrElse - not invoked "Calculate alternative"
  //maybeInt4.getOrElse - invoked "Calculate alternative"
  val resulMonad = maybeInt4.getOrElse({
    println("Calculate alternative")
    //буде калькуляція 1 , якщо викличиться OrElse
    1
  })

}
