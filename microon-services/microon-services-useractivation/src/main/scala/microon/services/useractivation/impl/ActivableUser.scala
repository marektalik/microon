package microon.services.useractivation.impl

import microon.services.usermanagement.api.scala.User

trait ActivableUser extends User {

  def active: Boolean

  def active(active: Boolean)

}
