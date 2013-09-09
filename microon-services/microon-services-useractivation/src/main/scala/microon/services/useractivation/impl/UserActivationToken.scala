package microon.services.useractivation.impl

trait UserActivationToken {

  def userId: Long

  def activationToken: String

}
