package net.study.functional.hw.oop.validator

import net.study.functional.hw.oop.errors.{EmptyStringError, Error, ValidationError}

import scala.util.matching.Regex

// Here you can declare all supplementary method for validation with style below(or use your own signature and return type
// if you want)
object ValidatorUtil {

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
