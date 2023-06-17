package net.study.functional.lesson

object Lesson2 extends App {
  //iterable - комплексне ітерування/проходження по елементам контейнера (hasNext, next)

  //traversable - просте проходження по елементам (example, foreach)

  //Monades (Option: Some, Node), Future, Map, Seq - containers

  //filter (many iteration) vs withFilter (1 itemation)
  //filter map => проходить 2 рази (1-ша ітерація - filter, 2-га ітерація - map)
  //withFilter map => вертає відфільтровану колекцію - 1 раз = map if satisfied withFilter

  //yield in for - as map in Stream

  //by value, by name - parameters in methods

  //comprehension
  //c1 == c2
  /*
  var c1 = for {
    a <- seqInt
    b <- seqInt
    c <- seqInt
  } yield {
    s"${a + b + c}"
  }
  */

  /*

    var c2 = seqInt.flatMap(a ->
      a.flatMap(b ->
        b.flatMap(c ->
          c.map(d -> a + b + c)
        )
      )
    )
  */


  val seqInt = Seq(1, 2, 3, 4, 5)

  //seqInt.foreach(println)

  for (x <- seqInt) {
    //  println(x)
  }

  val result = seqInt.map(x => x * x)

  val result2 = for {n <- seqInt} yield n * n

  val flattenResult = seqInt flatMap { n =>

    seqInt.map { m =>
      s"$n * $m = ${n * m}"
    }
  }

  val forComprehensionResult: Seq[String] = for {
    n <- seqInt
    m <- seqInt
  } yield {
    s"Vp = $n * $m * = ${n * m}"
  }

  // forComprehensionResult.foreach(println)

  val param1: Option[Long] = Some(1L)

  val param2: Option[Int] = Some(2)

  val param3: Option[String] = Some("2")

  val forOptionComprehensionResult: Option[(Long, Int, String)] = for {
    a <- param1
    b <- param2
    c <- param3
  } yield {
    (a, b, c + "d")
  }

  forOptionComprehensionResult.foreach(println)


  val seqInt2 = Seq(1, 2, 3, 4, 5)

  val multiplier = (x: Int) => x * x

  //RULES FOR FlATTENING
  println((seqInt2 map multiplier) == (seqInt2 flatMap (x => Seq(multiplier(x)))))

  val filterResult = seqInt2 filter (_ % 2 == 0) map multiplier

  val withFilterResult = seqInt2 withFilter (_ % 2 == 0) map multiplier

  println(filterResult == withFilterResult)

  val forOptionComprehensionWithGuardResult = for {

    x <- seqInt2 if x % 2 == 0

  } yield multiplier(x)

  println(forOptionComprehensionWithGuardResult == withFilterResult)

  val filterFunc = (a: Int) => a % 2 == 0

  def supplierMonad() = {
    println("I calculate monad")
    seqInt
  }

  val forComprehensionResultWithGuards: Seq[String] = for {
    a <- supplierMonad() if filterFunc(a)
    b <- supplierMonad() if filterFunc(b)
    c <- supplierMonad() if filterFunc(c)
  } yield {
    s"Vp = $a * $b * $c = ${a * b * c}"
  }

  forComprehensionResultWithGuards.foreach(println)


}
