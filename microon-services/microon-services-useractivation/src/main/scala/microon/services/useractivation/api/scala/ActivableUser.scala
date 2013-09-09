package microon.services.useractivation.api.scala

import microon.services.usermanagement.api.scala.User

trait ActivableUser extends User {

  def active: Boolean

  def active(active: Boolean)

}
