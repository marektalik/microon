package microon.services.usermanagement.impl.callback

import microon.services.usermanagement.api.scala.User
import scala.PartialFunction

trait BeforeUserRegistrationCallback[U <: User] {

  def callback: PartialFunction[U, Unit]

}

object BeforeUserRegistrationCallback {

  def apply[U <: User](callbackFunction: PartialFunction[U, Unit]): BeforeUserRegistrationCallback[U] = new BeforeUserRegistrationCallback[U] {
    def callback: PartialFunction[U, Unit] = callbackFunction
  }

}
