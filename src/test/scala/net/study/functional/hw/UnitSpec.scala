package net.study.functional.hw

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, EitherValues, FunSuite, GivenWhenThen, Ignore, Matchers, OptionValues, PartialFunctionValues, WordSpec}

//https://www.baeldung.com/scala/scalatest
//https://www.scalatest.org/user_guide
abstract class UnitSpec extends FunSuite
  with GivenWhenThen with Matchers with OptionValues
  with BeforeAndAfter with BeforeAndAfterEach
  with MockFactory with PartialFunctionValues
  with EitherValues {

  /*val builder = new StringBuilder

  before {
    builder.append("ScalaTest is ")
  }

  after {
    builder.clear()
  }

  override def beforeEach() {
    builder.append("ScalaTest is ")
    super.beforeEach() // To be stackable, must call super.beforeEach
  }

  override def afterEach() {
    try super.afterEach() // To be stackable, must call super.afterEach
    finally builder.clear()
  }*/
}
