package microon.services.usermanagement.api.scala

trait User {

  def id: java.lang.Long

  def active: Boolean

  def active(active: Boolean)

}
