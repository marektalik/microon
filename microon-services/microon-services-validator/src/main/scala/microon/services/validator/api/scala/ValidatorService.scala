package microon.services.validator.api.scala

import java.util.concurrent.Future

/**
 * Service dedicated for checking the state correctness and constrains against the business data (subject).
 */
trait ValidatorService {

  def validate(subject: Any): Future[Option[Array[String]]]

}