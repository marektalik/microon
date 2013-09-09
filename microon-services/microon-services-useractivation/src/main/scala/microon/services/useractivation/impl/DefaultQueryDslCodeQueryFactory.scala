package microon.services.useractivation.impl

import scalapi.querydsl.QueryDslPredicates
import com.mysema.query.types.{Predicate, Ops}

class DefaultQueryDslCodeQueryFactory extends CodeQueryFactory {

  def codeToQuery(code: String): Predicate =
    QueryDslPredicates.predicate("code", Ops.EQ, code)

}