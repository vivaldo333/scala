package net.study.functional.hw.oop.validator

import net.study.functional.hw.oop.errors.{EmptyStringError, Error, ValidationError}
import net.study.functional.hw.oop.services.LoginService
import net.study.functional.hw.oop.validator.RequestValidator.Name

import scala.util.matching.Regex

// Here you can declare all supplementary method for validation with style below(or use your own signature and return type
// if you want)
object ValidatorUtil {

  val loginService = new LoginService;
  def validateStringEmptyParam(paramName: String, maybeStringEmpty: Option[String]): Either[ValidationError, Unit] = {
    if (isEmpty(maybeStringEmpty)) Left(getValidationError(paramName, EmptyStringError))
    else
      Right(())
  }

  def validateParam(maybeStringEmpty: Option[String],
                    checkFunc: Option[String] => Boolean,
                    paramName: String,
                    checkError: Error): Either[ValidationError, Unit] = {
    if (checkFunc(maybeStringEmpty)) {
      Left(getValidationError(paramName, checkError))
    } else {
      Right(())
    }
  }

  /// ........
  def isEmpty(text: Option[String]): Boolean = {
    text.isEmpty || getOrEmpty(text).isEmpty
  }

  def isNotOnlyLatinCharacters(text: Option[String]): Boolean = {
    val latinPattern = "^[A-Za-z]+$".r
    !matchPattern(text, latinPattern)
  }

  def isNotOnlyDigits(text: Option[String]): Boolean = {
    val digitPattern = "^[0-9]+$".r
    !matchPattern(text, digitPattern)
  }

  def isNotLatinAndOrDigits(text: Option[String]): Boolean = {
    val latinAndDigitsPattern = "^[A-Za-z0-9]+$".r
    !matchPattern(text, latinAndDigitsPattern)
  }

  def isNotUniqueAcrossService(text: Option[String]): Boolean = {
    !text.exists(text => loginService.checkUniqueness(text))
  }

  def isNotPhoneLength(text: Option[String]): Boolean = {
    val textLength = getOrEmpty(text).length
    textLength != 9 && textLength != 12
  }

  /*def isPhone(text: Option[String]): Boolean = {
    !isEmpty(text) && isNotOnlyDigits(text) && isNotPhoneLength(text)
  }*/

  def matchPattern(text: Option[String], pattern: Regex): Boolean = {
    text.exists(str => pattern.pattern.matcher(str).matches())
  }

  def getOrEmpty(text: Option[String]): String = {
    text.getOrElse("")
  }

  def getValidationError(paramName: String, error: Error): ValidationError = {
    new ValidationError(paramName, error)
  }
}
