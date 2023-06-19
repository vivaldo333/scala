package net.study.functional.hw

import net.study.functional.hw.HomeWork2.PaymentCenter.getPaymentSum

object HomeWork2 extends  App {

  // if sum not submitted, precise in payment service.
    // In case not found remove from final report.
  // if tax sum is not assigned calculate it(20%)
    // (minimal tax free sum for calculation = 100
    // or reversal payments are tax free)
  // if desc is empty default value will be "technical"
  // dublicates must be removed
  // get method is not allowed, use for comprehension in main calculation method
  // output must be type of Seq[PaymentInfo] !!!!!!!!!
  lazy final val TAX_FEE: Double = 0.2
  lazy final val DEFAULT_DESCRIPTION = "technical"

  final case class PaymentInfoDto(paymentId: Int,
                                  customer: Option[String],
                                  sum: Option[Long],
                                  tax: Option[Long],
                                  desc: Option[String])

  final case class PaymentInfo(paymentId: Int,
                               sum: Long,
                               tax: Long,
                               desc: String)

  object PaymentCenter {
    def getPaymentSum(id: Int): Option[Long] = Option(id) filter (_ > 2) map (_ * 100)
  }

  val payments = Seq(
    PaymentInfoDto(1, Some("customerA"), Some(1500), None, Some("payment for Iphone 15")),
    PaymentInfoDto(2, Some("customerH"), None, None, Some("technical payment")),
    PaymentInfoDto(3, Some("customerB"), Some(99), None, Some("payment for headphone")),
    PaymentInfoDto(4, Some("customerC"), Some(1000), Some(200), None),
    PaymentInfoDto(5, Some("customerD"), Some(2500), None, None),
    PaymentInfoDto(6, Some("customerE"), Some(600), Some(120), Some("payment for Oculus quest 2")),
    PaymentInfoDto(7, Some("customerF"), Some(1500), None, Some("payment for Iphone 15")),
    PaymentInfoDto(8, Some("customerG"), Some(-400), None, Some("roll back transaction")),
    PaymentInfoDto(9, Some("customerH"), None, None, Some("some payment")),
    PaymentInfoDto(4, Some("customerC"), Some(1000), Some(200), None)
  )

  def getSum(): PaymentInfoDto => Option[Long] =
    paymentInfo => {
      paymentInfo.sum.orElse(PaymentCenter.getPaymentSum(paymentInfo.paymentId))
    }

  def getTax(): PaymentInfoDto => Option[Long] = {
    paymentInfo => {
      val sumForTax = getSum().apply(paymentInfo)
        .map(sum => if (sum > 100) sum else 0)
        .map(sum => (sum * TAX_FEE).toLong)
      paymentInfo.tax.orElse(sumForTax)
    }
  }

  def getDesc(): PaymentInfoDto => Option[String] = {
    paymentInfo => paymentInfo.desc
      .map(desc => if (desc == null || desc == "") DEFAULT_DESCRIPTION else  desc)
      .orElse(Some("technical"))
  }

  val toPaymentInfo = (paymentId: Int, sum: Long, tax: Long, desc: String) => PaymentInfo(paymentId, sum, tax, desc)

  var paymentWrapper = payments.distinct
          .flatMap(paymentInfo => {
            val paymentId = paymentInfo.paymentId
            val sum = getSum().apply(paymentInfo)
            val tax = getTax().apply(paymentInfo)
            val desc = getDesc().apply(paymentInfo)

            sum.flatMap(s =>
              tax.flatMap(t =>
                desc.map(d => toPaymentInfo(paymentId, s, t, d)))
            )
          })
          //.withFilter(payment => paymentWrapper._2.isDefined)

  //Alternate calculation with "get"
  var result: Seq[PaymentInfo] = for {
    p <- payments.distinct
    if getSum().apply(p).isDefined
  } yield toPaymentInfo(
    p.paymentId,
    getSum().apply(p).get,
    getTax.apply(p).getOrElse(0L),
    p.desc.getOrElse("technical")
  )

  //2 функції в одну: compose param, потім apply
  //compose

  //спочатку apply, потім andThen
  //andThen

  //Print result
  paymentWrapper.foreach(println)
  //result.foreach(println)

  //---Bogdan's solution-------------------------------------------
  val correctPayment = (id: Int, p: Option[Long]) => p orElse getPaymentSum(id)
  val calculateTax = (tax: Option[Long], sum: Long) => tax orElse Some(if (sum > 100) sum * 20 / 100 else 0)

  def correctPaymentInfo(paymentInfoDto: PaymentInfoDto): Option[PaymentInfo] = for {
    sumCalculated <- correctPayment(paymentInfoDto.paymentId, paymentInfoDto.sum)
    taxSum <- calculateTax(paymentInfoDto.tax, sumCalculated)
    desc <- paymentInfoDto.desc.orElse(Some("technical"))
  } yield PaymentInfo(paymentInfoDto.paymentId, sumCalculated, taxSum, desc)

  val result2: Seq[PaymentInfo] = (payments flatMap { x => correctPaymentInfo(x) }).distinct
  // in some cases if you need guarantee
  //val result: Seq[PaymentInfo] = (payments flatMap { x => correctPaymentInfo(x)}).toSet.toSeq
  result2.foreach(println)
}
