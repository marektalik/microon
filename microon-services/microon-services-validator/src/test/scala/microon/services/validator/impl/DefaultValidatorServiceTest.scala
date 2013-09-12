package microon.services.validator.impl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import StringMinimalLengthValidationSubjectHandler._
import microon.ri.activeobject.ImmediateActiveObjectDispatcher

@RunWith(classOf[JUnitRunner])
class DefaultValidatorServiceTest extends FunSuite {

  val validatorService = new DefaultValidatorService(Seq(new StringMinimalLengthValidationSubjectHandler()))
  validatorService.activeObjectDispatcher(new ImmediateActiveObjectDispatcher)

  test("Should trigger validation.") {
    expectResult(errorMessage) {
      validatorService.validate(shortString).get.get(0)
    }
  }

  test("Should not trigger validation.") {
    expectResult(0) {
      validatorService.validate("very long string").get.get.length
    }
  }

  test("Should not find handler.") {
    expectResult(None) {
      validatorService.validate(new Object).get
    }
  }

}

class StringMinimalLengthValidationSubjectHandler extends AbstractValidationSubjectHandler[String] {

  def supports(subject: Any): Boolean =
    subject.isInstanceOf[String]

  def doValidate(subject: String) {
    if (subject.length < minimalLength) {
      addValidationErrorMessage(errorMessage)
    }
  }

}

object StringMinimalLengthValidationSubjectHandler {
  val errorMessage = "errorMessage"
  val shortString = "short string"
  val minimalLength = shortString.length + 1
}