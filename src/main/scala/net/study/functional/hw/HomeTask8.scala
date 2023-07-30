package net.study.functional.hw

import scala.annotation.tailrec
import scala.util.Random

object HomeTask8 extends App {

  // your products
  trait Product

  case object CustomerInsurance extends Product

  case object LifeInsurance extends Product

  case object CreditPayment extends Product

  case object Charity extends Product

  case object CreditCardPayment extends Product

  type ProductType = Product

  //token information
  type PaymentToken = String

  type TokenProvider = Product => PaymentToken

  // your payment statuses
  trait Status

  case object Processed extends Status

  case object ClientMistake extends Status

  case object ServerMistake extends Status

  // if status code is absent in mapping
  case object Default extends Status

  // your initial request
  case class PaymentRequest(msisdn: String, productType: ProductType, tempCode: Int, paymentSum: Int)

  // your payment response
  case class PaymentResponse(code: Int, server: Int)

  // consolidated payment result
  case class PaymentResult(request: PaymentRequest, response: PaymentResponse)

  // final payment status
  case class PaymentStatus(request: PaymentRequest, status: Status)

  // NOT CHANGEABLE
  trait PaymentService {

    def geographicalPriority: Int

    def canProcess[T <: ProductType](product: T): Boolean

    def withdrawPayment(payment: PaymentRequest, tokenProvider: TokenProvider): PaymentResponse =
      someCostCalculation(payment)(prepared => send(prepared, tokenProvider(payment.productType)))

    protected def someCostCalculation(payment: PaymentRequest)(p: PaymentRequest => PaymentResponse): PaymentResponse = {
      // very difficult function
      p(payment)
    }

    protected def send(payment: PaymentRequest, token: PaymentToken): PaymentResponse = payment.tempCode match {
      case c if c >= 300 => PaymentResponse(500, geographicalPriority)
      case c if c % 2 == 0 => PaymentResponse(200, geographicalPriority)
      case c if c % 2 != 0 => PaymentResponse(400, geographicalPriority)
    }

    protected val products: Map[ProductType, Int] = Map(CustomerInsurance -> 1, LifeInsurance -> 2, CreditPayment -> 3, Charity -> 4)
  }

  trait PostPaymentService {

    def serviceStatus: Status

    def codesToProcess: Set[Int]

    def processResult(paymentResult: PaymentResult): Status =
    // do some post business logic
      serviceStatus
  }

  ////////////// YOUR available services   // NOT CHANGEABLE

  // pool of priority partner cluster system in your geographical position that can process request or you have to try with next one
  // if any of services can't process your request you have to save them fo further execution

  val serverPool: Seq[PaymentService] = Random.shuffle(1 to 4 map (x => new PaymentService {
    override val geographicalPriority: Int = x

    override def canProcess[T <: ProductType](product: T): Boolean = product match {
      case CreditCardPayment => false
      case _ => products.get(product).exists(_ <= geographicalPriority)
    }
  }))

  // HERE you can take a token   // NOT CHANGEABLE
  object SecurityServer {

    // secret token generation
    def generatePaymentToken(msisdn: String, product: Product, tempCode: Int): PaymentToken = {
      println("request to security server")
      (product.hashCode toString) + msisdn.hashCode + tempCode.hashCode()
    }
  }

  // input payments to proceed
  val payments = List(
    PaymentRequest("0670000001", CustomerInsurance, 222, 1000),
    PaymentRequest("0670000002", LifeInsurance, 122, 900),
    PaymentRequest("0670000003", CreditPayment, 121, 2000),
    PaymentRequest("0670000004", CreditPayment, 405, 2000),
    PaymentRequest("0670000005", Charity, 120, 100),
    PaymentRequest("0670000006", CreditCardPayment, 300, 500))

  //

  val statusCodes: Map[Status, Set[Int]] = Map(
    Processed -> Set(200, 201, 202, 204),
    ClientMistake -> Set(400, 401, 402, 403),
    ServerMistake -> Set(500, 501, 502, 503)
  )


  // your function for chaining you can use it in your code
  type BusinessDomain[-IN, +OUT] = PartialFunction[IN, OUT]

  def chainDomains[IN, OUT](domains: List[BusinessDomain[IN, OUT]], default: BusinessDomain[IN, OUT]): BusinessDomain[IN, OUT] = {
    @tailrec
    def chain(domains: List[BusinessDomain[IN, OUT]], acc: BusinessDomain[IN, OUT]): BusinessDomain[IN, OUT] = domains match {
      case Nil => acc
      case head :: tail => chain(tail, acc orElse head)
    }

    domains.headOption map (firstDomain => chain(domains.tail :+ default, firstDomain)) getOrElse default
  }

  type PaymentDomain = PartialFunction[PaymentRequest, PaymentResult]

  type PostPaymentDomain = PartialFunction[PaymentResult, PaymentStatus]

  // calculate this one ----------------------------------------------------------------------------------------------

  /*
  User story:

  1) Application use several partner servers to submit new payments in several time zones
  2) According to instance geographical position application ordered partner cluster servers with priority from nearest to more distant.
  servers of partners payment system.
  3) Partner payment server can deny some financial products sometimes for specific reasons, so if nearest server is not ready to process you pass it
  to the next one with next geographical priority which can process.
  4) If any server can't process catch it and store(logging it will be enough) for next reprocessing
  5) Then after payment was processed analyze answer's code in right PostPaymentService and calculate PaymentStatus. Mapping you can
  find in val statusCodes: Map[Status, Set[Int]]

  GENERAL TASK:
  implement next flow:

  PaymentRequest -> PartialFunction[PaymentRequest, PaymentResult] -> PartialFunction[PaymentResult, PaymentStatus] -> PaymentStatus
  So final function have to get PaymentRequest and return PaymentStatus !!!!!!!
  Resolve all payments with help of it.

  Tips:

  1)You have to call method canProcess[T <: Product](product: T): Boolean in PaymentService to ensure operation execution. This method not costly due to it cashing nature
  2)This task you can solve with help of design pattern "Chain of Responsibility"
  3) You can chain domains with help of "chainDomains" function
  4) Implement PostPaymentService to construct PostPaymentDomain
  5) Create methods that create PaymentDomain and PostPaymentDomain respectively
  6) use currying or PartialApplied Function to create TokenGenerator

  Mandatory conditions!!!!:
   Use ONLY PartialFunction to build execution flow
   */

  /////////////////////////////Implementation///////////////////////////////////////

  // declare types
  /*type PrePaymentDomain = PartialFunction[PaymentRequest, PaymentEnrichRequest]

  type AlternativePaymentDomain = PartialFunction[PaymentEnrichRequest, PaymentResult]*/

  // implementation PostPaymentService
  /*def getStatusCodes(status: Status): Set[Int] = statusCodes.getOrElse(Processed, Set.empty)

  val processedPostPaymentService = new PostPaymentService {
    override def serviceStatus: Status = Processed

    override def codesToProcess: Set[Int] = getStatusCodes(serviceStatus)
  }

  val clientMistakePostPaymentService = new PostPaymentService {
    override def serviceStatus: Status = ClientMistake

    override def codesToProcess: Set[Int] = getStatusCodes(serviceStatus)
  }

  val serverMistakePostPaymentService = new PostPaymentService {
    override def serviceStatus: Status = ServerMistake

    override def codesToProcess: Set[Int] = getStatusCodes(serviceStatus)
  }

  val defaultPostPaymentService = new PostPaymentService {
    override def serviceStatus: Status = Default

    override def codesToProcess: Set[Int] = Set.empty
  }*/

  //--
  //   PaymentRequest ->
  //   PartialFunction[PaymentRequest, PaymentResult] ->
  //   PartialFunction[PaymentResult, PaymentStatus] ->
  //   PaymentStatus

  //Test on PartialFunction
  //paymentDomain
  //postPaymentDomain
  //paymentFlow

  payments
  val sortedPaymentDomains = serverPool.sortBy(_.geographicalPriority) map createPaymentDomain toList
  val defaultPaymentDomain: PaymentDomain = {
    case req => PaymentResult(req, PaymentResponse(-1, -1))
  }

  val paymentDomain = chainDomains(sortedPaymentDomains, defaultPaymentDomain)

  def createPaymentDomain(server: PaymentService) : PaymentDomain = {
    case req if server.canProcess(req.productType) =>
      val tokenProvider: TokenProvider = SecurityServer.generatePaymentToken(req.msisdn, _:ProductType, req.tempCode)
      val paymentResponse = server.withdrawPayment(req, tokenProvider)
      PaymentResult(req, paymentResponse)
  }

  val postPaymentServices = statusCodes map {postPaymentCodeTuple =>
    val (status, codes) = postPaymentCodeTuple
    new PostPaymentService {
      override def serviceStatus: Status = status
      override def codesToProcess: Set[Int] = codes
    }
  } toList
  val defaultPostPaymentDomain: PostPaymentDomain = {
    case result => PaymentStatus(result.request, Default)
  }
  val postPaymentDomain: PostPaymentDomain = chainDomains(postPaymentServices map createPostPaymentDomain, defaultPostPaymentDomain)

  def createPostPaymentDomain(postPaymentService: PostPaymentService): PostPaymentDomain = {
    case paymentResult if postPaymentService.codesToProcess.contains(paymentResult.response.code) =>
      // here will be any complicated business logic for this status - for example, analytic, brokers
      PaymentStatus(paymentResult.request, postPaymentService.serviceStatus)
  }

  val paymentFlow = paymentDomain andThen postPaymentDomain

  val paymentStatuses = payments map paymentFlow

  paymentStatuses.foreach(println)


  //-- Token
  /*def generatePaymentTokenMixParam(msisdn: String, tempCode: Int, product: Product): PaymentToken =
    SecurityServer.generatePaymentToken(msisdn, product, tempCode)

  val tokenProviderWithAllParam: String => Int => Product => PaymentToken = (generatePaymentTokenMixParam _).curried

  // prepare Enum
  val postServiceByCode: Map[Int, PostPaymentService] = Map(
    200 -> processedPostPaymentService,
    201 -> processedPostPaymentService,
    202 -> processedPostPaymentService,
    204 -> processedPostPaymentService,
    400 -> clientMistakePostPaymentService,
    401 -> clientMistakePostPaymentService,
    402 -> clientMistakePostPaymentService,
    403 -> clientMistakePostPaymentService,
    500 -> serverMistakePostPaymentService,
    501 -> serverMistakePostPaymentService,
    502 -> serverMistakePostPaymentService,
    503 -> serverMistakePostPaymentService
  )

  //--
  case class PaymentEnrichRequest(req: PaymentRequest, server: Option[PaymentService])

  def canProcessPredicate(productType: ProductType): PaymentService => Boolean =
    service => service.canProcess(productType)

  val paymentEnrichRequestFunc: PrePaymentDomain = {
    case req if serverPool.exists(canProcessPredicate(req.productType)) =>
      val servers = serverPool.takeWhile(canProcessPredicate(req.productType))
      val server = if (servers.isEmpty) None else Some(servers.head)
      PaymentEnrichRequest(req, server)
  }

  val prePaymentDomainFunc: AlternativePaymentDomain = new AlternativePaymentDomain {
    override def isDefinedAt(enrichReq: PaymentEnrichRequest): Boolean =
      enrichReq.server.map(server =>
        server.withdrawPayment(enrichReq.req, tokenProviderWithAllParam(enrichReq.req.msisdn)(enrichReq.req.tempCode))).isDefined

    override def apply(enrichReq: PaymentEnrichRequest): PaymentResult = {
      val response = enrichReq.server match {
        case Some(server) => server.withdrawPayment(enrichReq.req, tokenProviderWithAllParam(enrichReq.req.msisdn)(enrichReq.req.tempCode))
        case _ => PaymentResponse(500, 1)
      }
      PaymentResult(enrichReq.req, response)
    }
  }*/
  /*case enrichReq =>
    val paymentResponse = enrichReq.server.map(server =>
        server.withdrawPayment(enrichReq.req, tokenProviderWithAllParam(enrichReq.req.msisdn)(enrichReq.req.tempCode)))
      .getOrElse(PaymentResponse(-1, -1))
    PaymentResult(enrichReq.req, paymentResponse)*/

  /*val postPaymentDomainFunc: PostPaymentDomain = {
    case result if postServiceByCode.isDefinedAt(result.response.code) =>
      val status = postServiceByCode.getOrElse(result.response.code, defaultPostPaymentService).processResult(result)
      PaymentStatus(result.request, status)
  }

  val result: Seq[PaymentStatus] = payments.map(payment =>
    postPaymentDomainFunc(
      prePaymentDomainFunc(
        paymentEnrichRequestFunc(payment)
      )
    )
  )

  println(result)*/
}
