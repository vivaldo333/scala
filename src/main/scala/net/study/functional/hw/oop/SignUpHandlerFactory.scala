package net.study.functional.hw.oop

import net.study.functional.hw.oop.handler.SignUpRequestHandler
import net.study.functional.hw.oop.mappers.Mappers

object SignUpHandlerFactory extends Mappers {
  def apply(): SignUpRequestHandler = new SignUpRequestHandler(){}
}
