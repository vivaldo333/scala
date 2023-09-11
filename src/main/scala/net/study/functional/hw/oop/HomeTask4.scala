package net.study.functional.hw.oop


import net.study.functional.hw.oop.handler.SignUpRequestHandler
import net.study.functional.hw.oop.mappers.Mappers
import net.study.functional.hw.oop.request.SignUpRequest
import net.study.functional.hw.oop.services.HashService

object HomeTask4 extends App with Mappers {

  /*
    Using all this infrustructure and fraims implement handler for SignUp operation
    1) validation, using rules declare in scala doc above SignUpRequest. All validation errors must be gathered together
    with help of ValidationError
    2) implement mapper for converting it to common SignUpDto and hash password
    3) implement processor(only simple stub which immediately returns OK answer)
    Write test for validator and mapper components
   */
  override val hashService: HashService = new HashService

  val signUpRequest = SignUpRequest(
    Some("name"),
    Some("surname"),
    Some("login1"),
    Some("pwd1"),
    Some("971112244"),
  )
  val signUpRequestHandler = new SignUpRequestHandler() {}
  val result = signUpRequestHandler.handle(signUpRequest).left.map(_.errorMessage)
  println(s"result: $result")
}




