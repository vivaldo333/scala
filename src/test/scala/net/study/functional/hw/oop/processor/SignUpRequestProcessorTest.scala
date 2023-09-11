package net.study.functional.hw.oop.processor

import net.study.functional.hw.UnitSpec
import net.study.functional.hw.oop.dto.SignUpDto
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SignUpRequestProcessorTest extends UnitSpec {

  val signUpRequestProcessor: SignUpRequestProcessor = new SignUpRequestProcessor() {}

  test("should successfully process SignUpDto") {
    val signUpData = SignUpDto("", "", "", "", "")

    val result = signUpRequestProcessor.process(signUpData)

    result.right.value.status shouldEqual OK
    result.right.value.dto shouldEqual signUpData
  }

}
