package net.study.functional.hw.oop.mappers

import net.study.functional.hw.UnitSpec
import net.study.functional.hw.oop.request.SignUpRequest
import net.study.functional.hw.oop.services.HashService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SignUpRequestMapperTest extends UnitSpec with Mappers {

  val mapper: SignUpRequestMapper = new SignUpRequestMapper() {}

  override val hashService: HashService = new HashService
  val name = "name"
  val surname = "surname"
  val login = "login"
  val pass = "pass"
  val msisdn = "msisdn"
  val signUpRequest: SignUpRequest = SignUpRequest(Some(name), Some(surname), Some(login), Some(pass), Some(msisdn))

  test("should convert SignUpRequest into SignUpDto") {
    val result = mapper.map(signUpRequest)

    result.right.value.name shouldEqual name
    result.right.value.surname shouldEqual surname
    result.right.value.login shouldEqual login
    result.right.value.secret shouldEqual hashService.hash(pass)
    result.right.value.msisdn shouldEqual msisdn
  }
}
