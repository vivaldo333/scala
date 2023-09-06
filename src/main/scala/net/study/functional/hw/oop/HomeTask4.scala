package net.study.functional.hw.oop


import net.study.functional.hw.oop.handler.SignUpRequestHandler
import net.study.functional.hw.oop.mappers.Mappers
import net.study.functional.hw.oop.request.{SignInRequest, SignUpRequest}

object HomeTask4 extends App with Mappers {


  /*
    Using all this infrustructure and fraims implement handler for SignUp operation
    1) validation, using rules declare in scala doc above SignUpRequest. All validation errors must be gathered together
    with help of ValidationError
    2) implement mapper for converting it to common SignUpDto and hash password
    3) implement processor(only simple stub which immediately returns OK answer)
    Write test for validator and mapper components
   */

  /*val signInRequestRequestHandler = new RequestHandler[SignInRequest, SignInDto, SignInResponse]
    with RequestValidator[SignInRequest] with Mapper[SignInRequest, SignInDto] with Processor[SignInDto, SignInResponse] {
    override def map(request: SignInRequest)(implicit defaultMapper: SignInRequest => SignInDto): Either[Error, SignInDto] = ???

    override def process(in: SignInDto): Either[Error, SignInResponse] = ???

    override def handle(request: SignInRequest)(implicit mapperFunc: SignInRequest => SignInDto): Either[Error, SignInResponse] = ???

    override def validate(request: SignInRequest): Either[Error, SignInRequest] = ???
  }*/

  /*val signInRequestRequestHandler = new RequestHandler[SignInRequest, SignInDto, SignInResponse]
  with SignInRequestValidator
  with SignInRequestMapper
  with SignInRequestProcessor
  with Mappers {

  }*/

  //valid - if Mappers is object (import object)
  //import net.study.functional.hw.oop.mappers.Mappers.signUpRequestMapper

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

  //SignUpHandlerFactory.apply()
}




