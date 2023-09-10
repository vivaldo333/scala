package net.study.functional.hw.oop.validator

import net.study.functional.hw.UnitSpec
import net.study.functional.hw.oop.errors.{EmptyStringError, GlobalError, ValidationError}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ValidatorUtilTest extends UnitSpec {
  val maybeStringEmpty: Option[String] = Some("TestName")
  val successCheckFunc: Option[String] => Boolean = _ => false
  val unsuccessCheckFunc: Option[String] => Boolean = _ => true
  val paramName: String = "Name"

  test("should validate SignUpRequest and return Unit when checkFunc returns false") {
    val result = ValidatorUtil.validateParam(maybeStringEmpty, successCheckFunc, paramName, GlobalError)
    result shouldBe Right(())
  }

  test("should validate SignUpRequest and return Error when checkFunc returns true") {
    val result = ValidatorUtil.validateParam(None, unsuccessCheckFunc, paramName, EmptyStringError)
    result should matchPattern {
      case Left(_: ValidationError) =>
    }
  }

  test("should return true for an empty Option") {
    val emptyOption: Option[String] = None
    val result = ValidatorUtil.isEmpty(emptyOption)
    result shouldBe true
  }

  test("should return true for an Option with an empty String") {
    val emptyStringOption: Option[String] = Some("")
    val result = ValidatorUtil.isEmpty(emptyStringOption)
    result shouldBe true
  }

  test("should return false for an Option with a non-empty String") {
    val nonEmptyStringOption: Option[String] = Some("Hello, World!")
    val result = ValidatorUtil.isEmpty(nonEmptyStringOption)
    result shouldBe false
  }

  test("should return true when text consists of not only latin symbols") {
    val nonOnlyLatinText = Some("abcабв")
    val result = ValidatorUtil.isNotOnlyLatinCharacters(nonOnlyLatinText)
    result shouldBe true
  }

  test("should return false when text consists of only latin symbols") {
    val nonOnlyLatinText = Some("abc")
    val result = ValidatorUtil.isNotOnlyLatinCharacters(nonOnlyLatinText)
    result shouldBe false
  }

  test("should return ValidationError with the provided paramName and error message") {
    val paramName = "exampleParam"
    val error = EmptyStringError

    val result = ValidatorUtil.getValidationError(paramName, error)

    result.errorMessage shouldEqual s"${error.errorMessage}: [$paramName]"
  }

  test("should return true if the text length is neither 9 nor 12") {
    val text1: Option[String] = Some("1234567890")
    val text2: Option[String] = Some("1234567890123")
    val text3: Option[String] = Some("12345")

    val result1 = ValidatorUtil.isNotPhoneLength(text1)
    val result2 = ValidatorUtil.isNotPhoneLength(text2)
    val result3 = ValidatorUtil.isNotPhoneLength(text3)

    result1 shouldBe true
    result2 shouldBe true
    result3 shouldBe true
  }

  test("should return false if the text length is 9") {
    val text: Option[String] = Some("123456789")

    val result = ValidatorUtil.isNotPhoneLength(text)

    result shouldBe false
  }

  test("should return false if the text length is 12") {
    val text: Option[String] = Some("123456789012")

    val result = ValidatorUtil.isNotPhoneLength(text)

    result shouldBe false
  }

  test("should return true if the text is empty") {
    val text: Option[String] = None

    val result = ValidatorUtil.isNotPhoneLength(text)

    result shouldBe true
  }
}
