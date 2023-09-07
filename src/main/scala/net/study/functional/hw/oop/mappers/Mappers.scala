package net.study.functional.hw.oop.mappers

import net.study.functional.hw.oop.dto.{SignInDto, SignUpDto, WorkInfoInDto}
import net.study.functional.hw.oop.request.{SignInRequest, SignUpRequest, WorkInfoRequest}
import net.study.functional.hw.oop.services.HashService

// here you can assign your implicit mapper function  implement this trait with your logic
trait Mappers {

  val hashService: HashService

  // implement this
  implicit val signUpRequestMapper: SignUpRequest => SignUpDto = (request: SignUpRequest) => {
    val name = getOrNull(request.name)
    val surname = getOrNull(request.surname)
    val login = getOrNull(request.login)
    val secret = getHashSecret(request.pass)
    val msisdn = getOrNull(request.msisdn)

    SignUpDto(name, surname, login, secret, msisdn)
  }
  //TODO(tsymbvol: 2023-09-06): implement
  implicit val signInRequestMapper: SignInRequest => SignInDto = (request: SignInRequest) => {
    val login = getOrNull(request.login)
    val secret = getHashSecret(request.password)
    SignInDto(login, secret)
  }
  //TODO(tsymbvol: 2023-09-06): implement
  implicit val workInfoRequestMapper: WorkInfoRequest => WorkInfoInDto = (request: WorkInfoRequest) => {
    val login = getOrNull(request.login)
    val workExperience = getOrEmpty(request.workExperience)
    val certificates = getOrEmpty(request.certificates)
    WorkInfoInDto(login, workExperience, certificates)
  }

  private[mappers] def getOrNull[A](attribute: Option[A]): A =
    attribute.getOrElse(null.asInstanceOf[A])

  private[mappers] def getOrEmpty[A](attribute: Option[List[A]]): List[A] =
    attribute.getOrElse(List.empty[A])
  private[mappers] def getHashSecret(pass: Option[String]): String = {
    getOrNull(pass map (secret => hashService.hash(secret)))
  }
}