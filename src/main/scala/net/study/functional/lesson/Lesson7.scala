package net.study.functional.lesson

import scala.annotation.tailrec

object Lesson7 extends App {

  //лістинг функції

  //high ordered function   definition
    //приймає 1 або більше об"єкті приймає в параметер list
    //або/і віддає функцію

  //tail recursion
    //повинна віддавати тип
    //повинна віддавати на цю ж функцію, без додаткових операцій: if .. else func (not 1 + func ot funt * 1 )

  //partial applied function
    //повинна завжди включати всі аргументи - аргумент і функцію

  //cyrried function
  //currying - перетворює функцію на list of functions with 1 arguments: func(1, 2, 3) => func_curried(1)(2)(3)
  //uncurrying - перетворює func_curried(1)(2)(3) => func(1, 2, 3)

  //partial function
    //доменна функція
    //new PartialFunction() contains 2 not implemented methods: isDefinedAt і apply
    //function will be run if "isDefinedAt" returns true
    /*
    object PartialFunctionsExample extends App {
      val division = new PartialFunction[Int, Int] {
          override def isDefinedAt(a: Int): Boolean = a != 0
          override def apply(v1: Int): Int = 100 / v1
      }
      //викликається isDefinedAt і apply
      println(division(0))

    }
    */

    val division: PartialFunction[Int, Int] = {
      //"isDefinedAt" method then apply method
      case x if x != 0 => 100 / x
    }
    //println(division(0))

  var divisionNotZero: PartialFunction[Int, Either[Int, Int]] = {
    case x if x != 0 => Right(100 / x)
  }

  val alternative: PartialFunction[Int, Either[Int, Int]] = {
    case _ => Left(0)
  }

  val divisionMain = alternative orElse divisionNotZero
  println(divisionMain(0))

  //patter matching on PartialFunction
  //одна функція
  //ніколи не вийдете за домен (перетворення PartialFunction в просту функцію)
  //а суть PartialFunction, щоб передати альтернативне виконання PartialFunction в іншу функцію - chain function
  //тобто в домені виконується тільки if (isDefinedAt) then apply()
  val divisionNotZeroSimpleFunction: PartialFunction[Int, Either[Int, Int]] = {
    case x if x != 0 => Right(100 / x)
    case x if x == 0 => Left(0)
  }

  val andThenFunction: PartialFunction[Either[Int, Int], Option[Int]] = {
    case x => x.toOption
  }

  //andThen method -> передати контроль кудись
  //"andThen" можна зробити за допомогою ліфтингу
  //"orElse" можна динамічно додавати (chaining динамічно генерувати - для динамічного керування доменами ... прийнявши на вхід функції)
  val divisionMain2 = (alternative orElse  divisionNotZero orElse alternative) andThen andThenFunction

  type MyDomainType = PartialFunction[Int, Either[Int, Int]]

  def chainPartialFunction(domains: List[MyDomainType]): Option[MyDomainType] = {
        @tailrec
        def chainDomains(domains: List[MyDomainType], acc: MyDomainType): MyDomainType = domains match {
          case Nil          => acc
          case head :: tail => chainDomains(tail, acc orElse head)
        }

        //dangerous
        //chainDomains(domains.tails, domains.head)
    domains.headOption match {
      case Some(firstDomain) => Option(chainDomains(domains.tail, firstDomain))
      case None => ???
    }
  }

  val domainList = List(divisionNotZero, alternative)
  val chainedDomainFunction = (chainPartialFunction(domainList)) getOrElse alternative
  println(chainedDomainFunction(10))

  //lifting
  //перетаскує з небезпечного sub-домену в безпечний sub-домен
  //трансформує PartialFunction в інший контейнер (Option)
  val liftedFunction: Int => Option[Either[Int, Int]] = divisionNotZero.lift
  val liftedFunction2: Int => Option[Either[Int, Int]] = (divisionNotZero orElse alternative) lift

  println(liftedFunction(0))
  //замість помилки вивело None
  println(liftedFunction2(0) map (x => x))

  val seqInt = Seq(1,2)
  //var param3 = seqInt(3)

  //UnitTest
  //PartialFunctionValues - used for testing PartialFunction
  //@Ignore - for ignore test


  //chain vs compose
  //trait Function1 має метод compose
  // func1 compose func2 => спочатку func2 до переданого аргументу, що перший зайшов, а потім викликається func1 викликаэться до розрахованого результату першої функції
  // func1 andThen func2 => спочатку визивається func1 і результат передається в func2 і виконується func2
  //andThen  - це chain і виконуюьться послідовно функції}
  // Function1 trait
  val square = (a: Int) => a * a
  val adder = (b: Int) => b * 2
  val composer = square compose adder
  val chained = square andThen adder
  println("composer: " + composer(2))
  println("chained: " + chained(3))
}