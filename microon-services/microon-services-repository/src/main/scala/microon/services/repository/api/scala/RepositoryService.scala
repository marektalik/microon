package microon.services.repository.api.scala

import java.io.Serializable
import java.util.concurrent.Future
import org.springframework.data.domain.{Pageable, Page, Sort}
import microon.spi.scala.activeobject.Void

trait RepositoryService[T, ID <: Serializable] {

  def save[S <: T](entity: S): Future[S]

  def save[S <: T](entities: Seq[S]): Future[Seq[S]]

  def findOne(id: ID): Future[T]

  def exists(id: ID): Future[Boolean]

  def findAll: Future[Seq[T]]

  def findAll(ids: Seq[ID]): Future[Seq[T]]

  def count: Future[Long]

  def delete(id: ID): Future[Void]

  def delete(entity: T): Future[Void]

  def delete(entities: Seq[_ <: T]): Future[Void]

  def deleteAll(): Future[Void]

  def findAll(sort: Sort): Future[Seq[T]]

  def findAll(pageable: Pageable): Future[Page[T]]

  def countByQuery(query: Any): Future[Long]

  def findAllByQuery(query: Any): Future[Seq[T]]

  def findAllByQuery(query: Any, pageable: Pageable): Future[Page[T]]

  def findAllByQuery(query: Any, sort: Sort): Future[Seq[T]]

  def findOneByQuery(query: Any): Future[T]

}