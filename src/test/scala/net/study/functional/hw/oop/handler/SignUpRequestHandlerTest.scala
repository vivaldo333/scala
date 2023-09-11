package net.study.functional.hw.oop.handler

import net.study.functional.hw.UnitSpec
import net.study.functional.hw.oop.mappers.Mappers
import net.study.functional.hw.oop.processor.OK
import net.study.functional.hw.oop.request.SignUpRequest
import net.study.functional.hw.oop.services.HashService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SignUpRequestHandlerTest extends UnitSpec with Mappers {

  val signUpRequestHandler: SignUpRequestHandler = new SignUpRequestHandler() {}

  override val hashService: HashService = new HashService
  val name = "name"
  val surname = "surname"
  val login = "login1"
  val pass = "pass1"
  val msisdn = "971112244"
  val signUpRequest: SignUpRequest = SignUpRequest(Some(name), Some(surname), Some(login), Some(pass), Some(msisdn))

  test("should return SignInResponse with OK response status when SignUpRequest is valid") {
    val result = signUpRequestHandler.handle(signUpRequest)

    result.right.value.status shouldEqual OK
    result.right.value.dto.name shouldEqual name
    result.right.value.dto.surname shouldEqual surname
    result.right.value.dto.login shouldEqual login
    result.right.value.dto.secret shouldEqual hashService.hash(pass)
    result.right.value.dto.msisdn shouldEqual msisdn
  }
}
