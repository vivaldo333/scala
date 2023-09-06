package net.study.functional.hw.oop.errors

// here you can declare your errors
sealed trait Error {

  def errorMessage: String
}


// mapper errors
case object LackMappedParamError extends Error {
  override val errorMessage: String = "lackMapperParam"
}

case object MapperError extends Error {
  override val errorMessage: String = "mapperError"
}

case object GlobalError extends Error {
  override def errorMessage: String = "globalError"
}

// validation Errors
case object EmptyStringError extends Error {
  override val errorMessage: String = "emptyError"
}

case object NonLatinSymbolsError extends Error {
  override val errorMessage: String = "nonLatinSymbolsError"
}

case object NonLatinOrAndDigitsSymbolsError extends Error {
  override val errorMessage: String = "nonLatinOrAndDigitsSymbolsError"
}

case object NonUniqueAcrossServiceError extends Error {
  override val errorMessage: String = "nonUniqueAcrossServiceError"
}

case object NotOnlyDigitsError extends Error {
  override val errorMessage: String = "notOnlyDigitsError"
}

case object NonValidPhoneLength extends Error {
  override val errorMessage: String = "nonValidPhoneLength"
}

// base trait for errors with merging effect
trait CombinedError[T <: CombinedError[T]] extends Error {

  def errors: Map[String, Error]

  def +(error: T): T

}

//
case class ValidationError(errors: Map[String, Error]) extends CombinedError[ValidationError] {
  require(errors.nonEmpty)

  private val spliterator = ", "

  def this(param: String, error: Error) = this(Map(param -> error))

  // TODO

  /**
   * implement this
   * This method has to merge this validationError with @param error
   *
   * @return new merged combined Validation Error
   */
  override def +(error: ValidationError): ValidationError =
    ValidationError(/*errors = */ this.errors ++ error.errors)

  // TODO
  /**
   * implement this
   * errorMessage using next pattern: -  errorMessage : [ field1,field2,....], errorMessage2: [field3,field4]
   *
   * @return merged combined Validation Error
   */
  //return accumulated errors into String
  override val errorMessage: String = {
    val groupedErrorsByMessage = errors groupBy {
      case (_, uniqueErrorMessage) => uniqueErrorMessage.errorMessage
    }
    groupedErrorsByMessage.map {
      case (key, values) =>
        s"$key: [${values.keys.mkString(spliterator)}]"
    }.mkString(spliterator)
  }

}