package net.study.functional.hw.oop.mappers

import net.study.functional.hw.oop.dto.{SignInDto, SignUpDto, WorkInfoInDto}
import net.study.functional.hw.oop.errors.{Error, GlobalError, MapperError}
import net.study.functional.hw.oop.exception.MapperException
import net.study.functional.hw.oop.request.{SignInRequest, SignUpRequest, WorkInfoRequest}
import net.study.functional.hw.oop.services.HashService

import scala.util.Try

private[oop] trait Mapper[R, DTO] {
  def map(request: R)(implicit defaultMapper: R => DTO): Either[Error, DTO]
}

private[oop] trait SignUpRequestMapper extends Mapper[SignUpRequest, SignUpDto] with Mappers {
  override val hashService: HashService = new HashService

  override def map(request: SignUpRequest)(implicit defaultMapper: SignUpRequest => SignUpDto): Either[Error, SignUpDto] =
    (Try(defaultMapper(request)) toEither).left.map {
      case _: MapperException => MapperError
      case unpredictable: Throwable => GlobalError
    }
}

private[oop] trait SignInRequestMapper extends Mapper[SignInRequest, SignInDto] with Mappers {
  //TODO(tsymbvol: 2023-09-06): implement
  override def map(request: SignInRequest)(implicit defaultMapper: SignInRequest => SignInDto): Either[Error, SignInDto] = ???
}

private[oop] trait WorkInfoRequestMapper extends Mapper[WorkInfoRequest, WorkInfoInDto] with Mappers {
  //TODO(tsymbvol: 2023-09-06): implement
  override def map(request: WorkInfoRequest)(implicit defaultMapper: WorkInfoRequest => WorkInfoInDto): Either[Error, WorkInfoInDto] = ???
}

