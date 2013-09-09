package microon.services.useractivation.api.scala

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait UserActivationService {

  def activateUser(userId: Long, code: String): Future[Void]

}
