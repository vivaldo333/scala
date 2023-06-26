package net.study.functional.hw

import net.study.functional.hw.HomeWork3.NetworkError

import java.io.Closeable
import scala.io.BufferedSource
import scala.io.Source.fromFile
import scala.util.{Failure, Success, Try}

object HomeWork3 extends App {
  //load data from csv-file
  //close resource
  //enrich data or invokes other method
  //sendToProvider -> returns Either[Error, Int], Int - count records that sent to Provider
  //Try transform into Either
  //Either transform into Try

  // 1) try to read file from external system over network (use method getFile with two columns:
  // 1) msisdn, subscriber type)
  // and don't forget to close resource after usage!!!!!!

  // 2) try to enrich get Data using main source getDataFromMainSource

  // 3) if fail to execute step 2) go to alternative source and try once more
  // ( use getDataFromAlternativeSource)

  // 4) if success to do so, try to send to 3-d party system all list

  // 5) Implement enrichAndSend method with proper Left(Error) type or Rigiht[Int]
  // Quantity of msisdns send to our third party system

  // Conditions:
  // use only Try Monad to resolve all problems with exception handling
  // You can use any additional custom functions / methods
  // Don't use method Try monad methods as get, getOrElse, isSuccess, isFailure !!!!!

  //tryGetSourceFile
  //tryEnrichSourceFileOrAlternative
  //trySendToSubscriber
  //for {} yield {}   -> Try to Either   ->  left map

  /// ===============help code ======================

  trait Error

  case class NetworkException(x: String) extends Exception

  case object NetworkError extends Error // if sftp server not available

  case object SourceTemporaryUnavailableError extends Error // if main source main source unavailable

  case object AllSourceTemporaryUnavailableError extends Error //if all source were unavailable

  case object ThirdPartySystemError extends Error //if 3-d party system error

  case class TemporaryUnavailableException(string: String) extends Exception

  case class ThirdPartySystemException(string: String) extends Exception

  case class SubscriberInfo(msisdn: String, subscriberType: Int, isActive: Boolean)

  val fileSource = "src/main/resources/lesson4/externalSourceFile.txt"

  // do not change this methods !!!!
  @throws[NetworkException]
  def getFile(isRisky: Boolean, source: String) =
    if (isRisky) throw NetworkException("SFTP server network exception") else fromFile(source)

  @throws[TemporaryUnavailableException]
  def getActiveData(isRisky: Boolean, msisdns: Seq[String]) =
    if (isRisky) throw TemporaryUnavailableException("Temporary Unavailable Exception") else {
      msisdns.map(m => (m, if (m.toInt % 2 == 0) 1 else 0))
    }

  @throws[TemporaryUnavailableException]
  def getDataFromMainSource(isRisky: Boolean, msisdns: Seq[String]) =
    getActiveData(isRisky, msisdns)

  @throws[TemporaryUnavailableException]
  def getDataFromAlternativeSource(isRisky: Boolean, msisdns: Seq[String]) =
    getActiveData(isRisky, msisdns)

  def sendToProvider(isRisky: Boolean, msisdns: Seq[SubscriberInfo]): Unit =
    if (isRisky) throw ThirdPartySystemException("third party system exception")
    else msisdns.foreach(m => s"Sent $m")

  // implement this one--------------------------------------------------------------------
  def usingSource[A, R <: Closeable](closeable: R)(body: R => A): A = try body(closeable) finally closeable.close()

  val lines = (source: BufferedSource) => Try {
    source.getLines()
      .map(line => line.split(";"))
      .map(dataArray => dataArray(0) -> dataArray(1).toInt)
      .toMap
  }

  def enrichAndSend(getFileIsRisky: Boolean,
                    getDataFromMainSourceIsRisky: Boolean,
                    getDataFromAlternativeSourceIsRisky: Boolean,
                    sendToProviderIsRisky: Boolean,
                    fileSource: String): Either[Error, Int] = {


    val originData: Try[Map[String, Int]] = usingSource(getFile(getFileIsRisky, fileSource)) {
      lines
    }

    def tryGetEnrichedData(data: Map[String, Int]) = {
      Try(getDataFromMainSource(getDataFromMainSourceIsRisky, data.keys.toList))
        .orElse(Try(getDataFromAlternativeSource(getDataFromAlternativeSourceIsRisky, data.keys.toList)))
        .map(d => d.map(msisdnAndIsActiveTuple =>
          SubscriberInfo(msisdnAndIsActiveTuple._1,
            data.getOrElse(msisdnAndIsActiveTuple._1, 0),
            if (msisdnAndIsActiveTuple._2 == 1) true else false
          ))
        )
    }

    def trySendToProvider(subscriberInfos: Seq[SubscriberInfo]): Try[Int] = {
      Try(sendToProvider(sendToProviderIsRisky, subscriberInfos))
        .map(_ => subscriberInfos.length)
    }


    val result = for {
      data <- originData.map(d => tryGetEnrichedData(d))
      subscriberInfos <- data
      r <- trySendToProvider(subscriberInfos)
    } yield r


    result match {
        case Failure(e: NetworkException) => Left(NetworkError)
        case Failure(e: TemporaryUnavailableException) => Left(SourceTemporaryUnavailableError)
        case Failure(e: ThirdPartySystemException) => Left(ThirdPartySystemError)
        case Failure(e) => Left(AllSourceTemporaryUnavailableError)
        case Success(v) => Right(v)
    }
  }

  var res = enrichAndSend(false, false, false, false, fileSource)
  println(res.right.map(x => x))

}
