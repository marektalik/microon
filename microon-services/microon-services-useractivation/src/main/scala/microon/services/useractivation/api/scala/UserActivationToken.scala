package microon.services.useractivation.api.scala

trait UserActivationToken {

  def userId: Long

  def activationToken: String

}
