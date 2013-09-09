package microon.services.useractivation.impl

trait UserActivationTokenFactory[T] {

  def createToken(userId: Long, token: String): T

}
