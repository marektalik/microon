package microon.services.validator.impl

import microon.services.validator.api.scala.ValidatorService
import java.util.concurrent.Future
import microon.spi.scala.activeobject.{ActiveObjectDispatcher, ActiveObject}
import scala.collection.JavaConversions._

/**
 * Default implementation of [[microon.services.validator.api.scala.ValidatorService]] delegating the validation
 * process to the particular [[microon.services.validator.impl.ValidationSubjectHandler]] instance supporting given
 * subject.
 *
 * @param subjectHandlers sequence of [[microon.services.validator.impl.ValidationSubjectHandler]] instances responsible
 *                        for the actual validation.
 */
class DefaultValidatorService(subjectHandlers: Seq[_ <: ValidationSubjectHandler[_]])
  extends ValidatorService with ActiveObject {

  def validate(subject: Any): Future[Option[Array[String]]] = dispatch {
    val supportedHandlers = subjectHandlers.filter(_.supports(subject))
    if (supportedHandlers.isEmpty) {
      None
    } else {
      Some(supportedHandlers.head.asInstanceOf[ValidationSubjectHandler[Any]].validate(subject))
    }
  }

}

object DefaultValidatorService {

  def apply(contextHandlers: java.util.List[ValidationSubjectHandler[_]], dispatcher: ActiveObjectDispatcher) = {
    val validatorService = new DefaultValidatorService(contextHandlers)
    validatorService.activeObjectDispatcher(dispatcher)
    validatorService
  }

}