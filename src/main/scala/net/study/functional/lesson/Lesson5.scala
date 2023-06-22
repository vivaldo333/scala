package net.study.functional.lesson

object Lesson5 extends App {
  //Pattern matching
  val intSeq: Seq[Int] = 1 to 10

  //1) Simple constant matching
  val maybeInt: AnyVal = 10

  maybeInt match {
    case 1 => println(1)
    case 10.0F => println(10)
    case true => println("bool")
    case _ => println()
  }

  //2) Case class matching
  case class Person(name: String, age: Int);

  val person= Person("Person", 20)

  person match {
    //unapply (decomposition)
    case Person(name, age) =>
    case _ =>
  }

  //3) Tuple matching
  //Tuple - container with different defined types (by default exists 22 typles)
  val simpleTuple: (Int) = (1)
  val tuple2: (Int, String) = (1, "2")
  //розаплаяти Tuple
  val (myInt, myString) = tuple2

  tuple2 match {
    case (myInt, myString) =>
    case _ =>
  }

  var simpleTuplePerson: (String, Int) = ("Andriy", 22)
  //(Person.apply _) - означає візьми функцію "apply", але не виконуй
  //apply - метод, що буде згенерований компілятором
  // tupled - застосуй Tuple, який підходить по атрибутам
  //convert Tuple to Person case class
  val toPerson = (Person.apply _)  tupled

  println(toPerson(simpleTuplePerson))

  case class IrregularNumber(up: Int, down: Int) {
    //override "+" method
    def + (d: IrregularNumber) = ???
  }

  //IrregularNumber(1, 4) + IrregularNumber(2, 5)

}
