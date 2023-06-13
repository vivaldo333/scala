package net.study.functional.hw.hw001

//scala HelloYou
//scala HelloYou Al
object Hello2 extends App {
  if (args.size == 0)
    println("Hello, you")
  else
    println("Hello, " + args(0))
}
