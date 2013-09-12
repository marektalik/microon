package microon.services.validator.api.scala

import java.util.concurrent.Future

trait ValidatorService {

  def validate(subject: Any): Future[Option[Array[String]]]

}