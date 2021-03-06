package microon.services.repository.impl

import microon.services.repository.api.scala.RepositoryService
import org.springframework.data.domain.{Sort, Pageable}
import java.util.concurrent.Future
import microon.spi.scala.activeobject.{Void, ActiveObject}
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable
import scala.collection.JavaConversions._

class DefaultRepositoryService[T, ID <: Serializable]
(repository: PagingAndSortingRepository[T, ID], queryHandler: QueryHandler[T])
  extends RepositoryService[T, ID]
  with ActiveObject {

  def save[S <: T](entity: S): Future[S] = dispatch {
    repository.save(entity)
  }

  def saveMany[S <: T](entities: Seq[S]): Future[Seq[S]] = dispatch {
    repository.save(entities).toSeq
  }

  def findOne(id: ID): Future[Option[T]] = dispatch {
    Option(repository.findOne(id))
  }

  def exists(id: ID): Future[Boolean] = dispatch {
    repository.exists(id)
  }

  def findAll: Future[Seq[T]] = dispatch {
    repository.findAll().toSeq
  }

  def findAll(ids: Seq[ID]): Future[Seq[T]] = dispatch {
    repository.findAll(ids).toSeq
  }

  def count: Future[Long] = dispatch {
    repository.count
  }

  def delete(id: ID): Future[Void] = void {
    repository.delete(id)
  }

  def delete(entity: T): Future[Void] = void {
    repository.delete(entity)
  }

  def delete(entities: Seq[_ <: T]): Future[Void] = void {
    repository.delete(entities)
  }

  def deleteAll(): Future[Void] = void {
    repository.deleteAll()
  }

  def findAll(sort: Sort): Future[Seq[T]] = dispatch {
    repository.findAll(sort).toSeq
  }

  def findAll(pageable: Pageable): Future[Seq[T]] = dispatch {
    repository.findAll(pageable).getContent
  }

  def countByQuery(query: Any): Future[Long] = dispatch {
    queryHandler.countByQuery(query)
  }

  def findAllByQuery(query: Any): Future[Seq[T]] = dispatch {
    queryHandler.findAllByQuery(query)
  }

  def findAllByQuery(query: Any, pageable: Pageable): Future[Seq[T]] = dispatch {
    queryHandler.findAllByQuery(query, pageable)
  }

  def findOneByQuery(query: Any): Future[T] = dispatch {
    queryHandler.findOneByQuery(query)
  }

}