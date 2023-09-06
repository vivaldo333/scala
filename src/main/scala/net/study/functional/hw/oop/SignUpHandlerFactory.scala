package net.study.functional.hw.oop

import net.study.functional.hw.oop.handler.SignUpRequestHandler
import net.study.functional.hw.oop.mappers.Mappers

object SignUpHandlerFactory extends Mappers {
  //TODO(tsymbvol: 2023-09-06): implement alternative of HomeTask4
  def apply(): SignUpRequestHandler = new SignUpRequestHandler() {}
}
