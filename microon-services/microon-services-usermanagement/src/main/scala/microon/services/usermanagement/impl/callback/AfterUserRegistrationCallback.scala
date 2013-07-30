package microon.services.usermanagement.impl.callback

import microon.services.usermanagement.api.scala.User
import scala.PartialFunction

trait AfterUserRegistrationCallback[U <: User] {

  def callback: PartialFunction[U, Unit]

}

object AfterUserRegistrationCallback {

  def apply[U <: User](callbackFunction: PartialFunction[U, Unit]): AfterUserRegistrationCallback[U] = new AfterUserRegistrationCallback[U] {
    def callback: PartialFunction[U, Unit] = callbackFunction
  }

}