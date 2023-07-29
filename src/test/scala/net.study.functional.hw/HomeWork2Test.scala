package net.study.functional.hw

import org.junit.runner.RunWith
import org.scalatest.Inside.inside
import org.scalatest.junit.JUnitRunner
import net.study.functional.hw.HomeWork2._

@RunWith(classOf[JUnitRunner])
class HomeWork2Test extends UnitSpec {

  val paymentId: Int = 10
  val customer: String = "Customer"
  val paymentSum: Long = 1000L
  val paymentTax: Long = 10L
  val paymentDesc: String = "Transfer"
  val paymentInfo = PaymentInfoDto(paymentId, Some(customer), Some(paymentSum), Some(paymentTax), Some(paymentDesc))

  test("payment sum should have positive result when sum present ") {
    val sumResult = getSum().apply(paymentInfo)
    sumResult should be ('defined)
    assert(sumResult.value == paymentSum)
  }

  test("payment sum should have positive result when sum is not present ") {
    val expectedSum = paymentId * 100
    val paymentInfoWithoutSum = PaymentInfoDto(paymentId, Some(customer), None, Some(paymentTax), Some(paymentDesc))
    val sumResult = getSum().apply(paymentInfoWithoutSum)
    sumResult should be('defined)
    sumResult.value shouldEqual expectedSum
  }

  test("payment tax should be zero when initial tax is not defined and sum is not greater one hundred") {
    val paymentInfoWithoutSumLess100 = PaymentInfoDto(paymentId, Some(customer), Some(90L), None, Some(paymentDesc))
    val taxResult = getTax().apply(paymentInfoWithoutSumLess100)
    taxResult should be('defined)
    assert(taxResult.value == 0)
  }

  test("payment tax should be zero when initial tax is not defined and sum is greater one hundred") {
    val paymentSum = 120L
    val expectedTax = (paymentSum * 0.2).toLong
    val paymentInfoWithoutSumLess100 = PaymentInfoDto(paymentId, Some(customer), Some(paymentSum), None, Some(paymentDesc))
    val taxResult = getTax().apply(paymentInfoWithoutSumLess100)
    taxResult should be('defined)
    assert(taxResult.value == expectedTax)
  }

  test("payment tax should be zero when initial tax is defined") {
    val taxResult = getTax().apply(paymentInfo)
    taxResult should be('defined)
    taxResult.value shouldEqual paymentTax
  }

  test("payment description should be not default when description is defined") {
    val taxResult = getDesc().apply(paymentInfo)
    taxResult should be('defined)
    taxResult.value shouldEqual paymentDesc
  }

  test("payment description should be default when description is not defined") {
    val expectedDefaultPaymentDesc = "technical"
    val paymentInfoWithoutDesc = PaymentInfoDto(paymentId, None, None, None, None)
    val taxResult = getDesc().apply(paymentInfoWithoutDesc)
    taxResult should be('defined)
    taxResult.value shouldEqual expectedDefaultPaymentDesc
  }

  test("payment info attributes should be defined") {
    val paymentInfoResult = correctPaymentInfo(paymentInfo)
    inside (paymentInfoResult.value) { case PaymentInfo(id, sum, tax, desc) =>
        id should be (paymentId)
        sum should be (paymentSum)
        tax should be (paymentTax)
        desc should be (paymentDesc)
    }
  }
}
