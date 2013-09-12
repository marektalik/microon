package microon.services.validator.impl

trait ValidationSubjectHandler[S <: Any] {

  def supports(subject: Any): Boolean

  def validate(subject: S): Array[String]

}
