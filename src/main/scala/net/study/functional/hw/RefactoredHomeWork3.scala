package net.study.functional.hw

import java.io.Closeable
import scala.io.BufferedSource
import scala.io.Source.fromFile
import scala.util.Try

object RefactoredHomeWork3 extends App {

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

  //---Implement-----------------------------------------------------------------------------------------------------------

  case object OtherError extends Error

  val toSubscriberInfoList = (line: String) =>
    line.split(";")
      .map(_.trim)
      .filter(_.length == 2)
      .map(arr => SubscriberInfo(arr(0).toString, arr(1).toInt, isActive = false))
      .toList

  def getBufferedData(isRisky: Boolean, source: String): Either[Error, BufferedSource] = {
    try Right(getFile(isRisky, source))
    catch {
      case e: NetworkException => Left(NetworkError)
      case _: Throwable => Left(OtherError)
    }
  }

  def getSubscribers(bufferData: BufferedSource): Either[Error, Seq[SubscriberInfo]] = {
    try Right(usingSource(bufferData)(bufferData =>
      ((bufferData.getLines() map toSubscriberInfoList) flatten) toList))
      catch {
        case _: Throwable => Left(OtherError)
        case e: Error => Left(e)
      }
  }

  def usingSource[A, R <: Closeable](closeable: R)(body: R => A): A = try body(closeable) finally closeable.close()

  def getEnrichmentSource(isRiskyMain: Boolean, isRiskySecond: Boolean, subscribers: Seq[SubscriberInfo]): Either[Error, Map[String, Int]] = {
    val msisdns = subscribers.map(subscriber => subscriber.msisdn).toList
    ((Try(getDataFromMainSource(isRiskyMain, msisdns))
      orElse Try(getDataFromAlternativeSource(isRiskySecond, msisdns))) map(_.toMap) toEither).left.map {
        case _: TemporaryUnavailableException => AllSourceTemporaryUnavailableError
        case unpredictable: Throwable => OtherError
      }
  }

  val enrichSubscriber = (subscriberInfo: SubscriberInfo, searchResult: Option[Int]) =>
    searchResult map (activeState => subscriberInfo.copy(isActive = activeState == 1)) orElse Some(subscriberInfo)

  def enrichSubscribers(subscribers: Seq[SubscriberInfo], enrichmentSource: Map[String, Int]): Either[Error, Seq[SubscriberInfo]] = {
    try Right(for {
      poorSubscriber <- subscribers
      richSubscriber <- enrichSubscriber(poorSubscriber, enrichmentSource.get(poorSubscriber.msisdn))
    } yield richSubscriber) catch {
      case _: Throwable => Left(OtherError)
      case e: Error => Left(e)
    }
  }

  def tryToSendToProvider(isRisky: Boolean, subscribers: Seq[SubscriberInfo]): Either[Error, Unit] = {
    try Right(sendToProvider(isRisky, subscribers))
    catch {
      case _: ThirdPartySystemException => Left(ThirdPartySystemError)
      case _: Throwable => Left(OtherError)
      case e: Error => Left(e)
    }
  }

  def enrichAndSend(
                     getFileIsRisky: Boolean,
                     getDataFromMainSourceIsRisky: Boolean,
                     getDataFromAlternativeSourceIsRisky: Boolean,
                     sendToProviderIsRisky: Boolean,
                     fileSource: String
                   ): Either[Error, Int] = for {
    bufferedData <- getBufferedData(getFileIsRisky, fileSource)
    subscribers <- getSubscribers(bufferedData)
    enrichedSource <- getEnrichmentSource(getDataFromMainSourceIsRisky, getDataFromAlternativeSourceIsRisky, subscribers)
    enrichedSubscribers <- enrichSubscribers(subscribers, enrichedSource)
    _ <- tryToSendToProvider(sendToProviderIsRisky, enrichedSubscribers)
  } yield 1

  println(enrichAndSend(false, false, false, false, fileSource))

}
