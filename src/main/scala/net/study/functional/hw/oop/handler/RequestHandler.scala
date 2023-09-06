package net.study.functional.hw.oop.handler

import net.study.functional.hw.oop.dto.{SignInDto, SignUpDto}
import net.study.functional.hw.oop.errors.Error
import net.study.functional.hw.oop.mappers.{Mapper, SignInRequestMapper, SignUpRequestMapper}
import net.study.functional.hw.oop.processor.{Processor, SignInRequestProcessor, SignUpRequestProcessor}
import net.study.functional.hw.oop.request.{SignInRequest, SignUpRequest}
import net.study.functional.hw.oop.response.{SignInResponse, SignUpResponse}
import net.study.functional.hw.oop.validator.{RequestValidator, SignInRequestValidator, SignUpRequestValidator}


// implement this abstraction use self type trait mixin to implement validate -> map -> process logic
// you can't change signature or self type abstraction
trait RequestHandler[R, DTO, RESP] {

  this: RequestValidator[R] with Mapper[R, DTO] with Processor[DTO, RESP] =>

  def handle(request: R)(implicit mapperFunc: R => DTO): Either[Error, RESP] = {
    for {
      validatedRequest <- validate(request)
      dto <- map(validatedRequest)
      response <- process(dto)
    } yield response
  }

}

/*trait SignInRequestRequestHandler extends RequestHandler[SignInRequest, SignInDto, SignInResponse]
    with Mapper[SignInRequest, SignInDto] with Processor[SignInDto, SignInResponse] {
  this: RequestValidator[SignInRequest] with Mapper[SignInRequest, SignInDto] with Processor[SignInDto, SignInResponse] =>

  override def handle(request: SignInRequest)(implicit mapperFunc: SignInRequest => SignInDto): Either[Error, SignInResponse] = ???
}*/

trait SignInRequestRequestHandler extends RequestHandler[SignInRequest, SignInDto, SignInResponse]
  with SignInRequestValidator with SignInRequestMapper with SignInRequestProcessor
  //{
  //override def handle(request: SignInRequest)(implicit mapperFunc: SignInRequest => SignInDto): Either[Error, SignInResponse] = ???
  /*override def handle(request: SignInRequest)(implicit mapperFunc: SignInRequest => SignInDto): Either[Error, SignInResponse] =
    super.handle(request)*/
//}

trait SignUpRequestHandler extends RequestHandler[SignUpRequest, SignUpDto, SignUpResponse]
  with SignUpRequestValidator with SignUpRequestMapper with SignUpRequestProcessor