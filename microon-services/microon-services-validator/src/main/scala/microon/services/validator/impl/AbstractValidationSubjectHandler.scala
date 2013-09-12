package microon.services.validator.impl

import scala.collection.mutable.ListBuffer

abstract class AbstractValidationSubjectHandler[S] extends ValidationSubjectHandler[S] {

  private val validationErrorMessages = new ThreadLocal[ListBuffer[String]]() {
    override def initialValue() = ListBuffer[String]()
  }

  protected def resetValidationErrorMessages() {
    validationErrorMessages.get.clear()
  }

  protected def addValidationErrorMessage(message: String) {
    validationErrorMessages.get += message
  }

  def validate(subject: S): Array[String] = {
    resetValidationErrorMessages()
    doValidate(subject)
    validationErrorMessages.get.toArray
  }

  def doValidate(subject: S): Unit

}
