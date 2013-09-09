package microon.services.useractivation.impl

import microon.spi.scala.activeobject.{Void, ActiveObject}
import java.util.concurrent.Future
import microon.services.repository.api.scala.RepositoryService
import java.lang.Long
import microon.services.useractivation.api.scala.UserActivationService

class DefaultUserActivationService[U <: ActivableUser, T <: UserActivationToken]
(userRepositoryService: RepositoryService[U, Long],
 tokenRepositoryService: RepositoryService[T, java.lang.Long],
 codeQueryFactory: CodeQueryFactory)
  extends UserActivationService with ActiveObject {

  def activateUser(userId: scala.Long, code: String): Future[Void] = void {
    val token = tokenRepositoryService.findOneByQuery(codeQueryFactory.codeToQuery(code)).get
    if (token.userId == userId) {
      val user = userRepositoryService.findOne(userId).get.get
      user.active(true)
      userRepositoryService.save(user)
    }
  }

}