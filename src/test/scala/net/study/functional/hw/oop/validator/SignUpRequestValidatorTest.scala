package net.study.functional.hw.oop.validator

import net.study.functional.hw.UnitSpec
import net.study.functional.hw.oop.request.SignUpRequest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SignUpRequestValidatorTest extends UnitSpec {

  val validator: SignUpRequestValidator = new SignUpRequestValidator() {}

  test("should successfully validate SignUpRequest") {
    val signUpRequest = SignUpRequest(
      Some("name"),
      Some("surname"),
      Some("login1"),
      Some("pwd1"),
      Some("971112244"),
    )

    val result = validator.validate(signUpRequest)

    result.right.value shouldEqual signUpRequest
  }

  test("should validate SignUpRequest and return Errors") {
    val signUpRequest = SignUpRequest(
      None,
      None,
      None,
      Some("pwd1"),
      None,
    )
    val expectedErrorMessage = "emptyError: [login0, surname0, name0, msisdn0], nonUniqueAcrossServiceError: [login3], nonValidPhoneLength: [msisdn3], nonLatinSymbolsError: [surname1, name1], notOnlyDigitsError: [msisdn1], nonLatinOrAndDigitsSymbolsError: [login1]"

    val result = validator.validate(signUpRequest)

    result.left.value.errorMessage shouldEqual expectedErrorMessage
  }

}
