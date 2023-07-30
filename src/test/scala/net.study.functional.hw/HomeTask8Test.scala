package net.study.functional.hw

import net.study.functional.hw.HomeTask8._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HomeTask8Test extends UnitSpec {
  private[HomeTask8Test] val paymentRequest = PaymentRequest("0670000001", CustomerInsurance, 222, 1000)
  private[HomeTask8Test] val paymentResult = PaymentResult(paymentRequest, PaymentResponse(200, 1))
  private[HomeTask8Test] val paymentStatus = PaymentStatus(paymentRequest, Processed)

  test("should return PaymentResult when input parameter is PaymentRequest") {
    paymentDomain.valueAt(paymentRequest) should equal(paymentResult)
  }

  test("should return PaymentStatus when input parameter is PaymentResult") {
    assert(postPaymentDomain.valueAt(paymentResult) === paymentStatus)
  }

  test("should return PaymentStatus when input parameter is PaymentRequest") {
    paymentFlow.valueAt(paymentRequest) should equal(paymentStatus)
  }
}
