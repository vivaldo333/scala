package net.study.functional.hw.oop.processor

import net.study.functional.hw.oop.dto.{SignInDto, SignUpDto, WorkInfoInDto}
import net.study.functional.hw.oop.errors.Error
import net.study.functional.hw.oop.response.{SignInResponse, SignUpResponse, WorkInfoResponse}

trait Processor[DTO, RESP] {

  def process(in: DTO): Either[Error, RESP]

}


//  Response status Always wil be ok for just now
trait Status

case object OK extends Status

trait SignUpRequestProcessor extends Processor[SignUpDto, SignUpResponse] {
  override def process(in: SignUpDto): Either[Error, SignUpResponse] =
    Right(SignUpResponse(OK, in))
}

trait SignInRequestProcessor extends Processor[SignInDto, SignInResponse] {
  //TODO(tsymbvol: 2023-09-06): implement
  override def process(in: SignInDto): Either[Error, SignInResponse] = ???
}

trait WorkInfoProcessor extends Processor[WorkInfoInDto, WorkInfoResponse] {
  //TODO(tsymbvol: 2023-09-06): implement
  override def process(in: WorkInfoInDto): Either[Error, WorkInfoResponse] = ???
}