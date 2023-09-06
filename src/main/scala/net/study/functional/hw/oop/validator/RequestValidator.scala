package net.study.functional.hw.oop.validator

import net.study.functional.hw.oop.errors.{EmptyStringError, Error, NonLatinOrAndDigitsSymbolsError, NonLatinSymbolsError, NonUniqueAcrossServiceError, NonValidPhoneLength, NotOnlyDigitsError}
import net.study.functional.hw.oop.request.{SignInRequest, SignUpRequest, WorkInfoRequest}
import net.study.functional.hw.oop.validator.RequestValidator.{Login, Msisdn, Name, Surname}
import net.study.functional.hw.oop.validator.ValidatorUtil.validateParam


// here you can implement sub-traits for validation purpose


trait RequestValidator[R] {

  def validate(request: R): Either[Error, R] // скомпоузити ейзери в ліcт а потім flatten те що потрібно (помилки) далі reduceLeft

}

object RequestValidator {
  //form constant - for put into error
  val Name = "name"
  val Surname = "surname"
  val Login = "login"
  val pass = "pass"
  val Msisdn = "msisdn"
}

trait SignUpRequestValidator extends RequestValidator[SignUpRequest] {
  override def validate(request: SignUpRequest): Either[Error, SignUpRequest] = {
    val validationResult = List(
      validateParam(request.name, ValidatorUtil.isEmpty, Name+0, EmptyStringError),
      validateParam(request.name, ValidatorUtil.isNotOnlyLatinCharacters, Name+1, NonLatinSymbolsError),
      validateParam(request.surname, ValidatorUtil.isEmpty, Surname+0, EmptyStringError),
      validateParam(request.surname, ValidatorUtil.isNotOnlyLatinCharacters, Surname+1, NonLatinSymbolsError),
      validateParam(request.login, ValidatorUtil.isEmpty, Login+0, EmptyStringError),
      validateParam(request.login, ValidatorUtil.isNotLatinAndOrDigits, Login+1, NonLatinOrAndDigitsSymbolsError),
      validateParam(request.login, ValidatorUtil.isNotUniqueAcrossService, Login+3, NonUniqueAcrossServiceError),
      validateParam(request.msisdn, ValidatorUtil.isEmpty, Msisdn+0, EmptyStringError),
      validateParam(request.msisdn, ValidatorUtil.isNotOnlyDigits, Msisdn+1, NotOnlyDigitsError),
      validateParam(request.msisdn, ValidatorUtil.isNotPhoneLength, Msisdn+3, NonValidPhoneLength)
    )
    //val aggregatedValidationError = (validationResult map (e => e.swap.toOption) flatten) reduceLeft (_ + _)
    //val rr = if (aggregatedValidationError.errors.nonEmpty) Left(aggregatedValidationError) else Right(request)
    validationResult flatMap (_.swap.toOption) match {
      case Nil => Right(request)
      case /*::(head, tl)*/ head :: tl => Left(tl.foldLeft(head)(_+_))
    }
  }
}

trait SignInRequestValidator extends RequestValidator[SignInRequest] {
  override def validate(request: SignInRequest): Either[Error, SignInRequest] = ???
}

trait WorkInfoRequestValidator extends RequestValidator[WorkInfoRequest] {
  override def validate(request: WorkInfoRequest): Either[Error, WorkInfoRequest] = ???
}