package net.study.functional.hw.oop.exception

// Here you can declare your exceptions
case class MapperException(errMsg: String) extends RuntimeException(errMsg)
