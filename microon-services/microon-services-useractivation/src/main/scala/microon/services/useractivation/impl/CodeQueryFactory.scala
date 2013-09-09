package microon.services.useractivation.impl


trait CodeQueryFactory {

  def codeToQuery(code: String): Any

}
