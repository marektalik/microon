package microon.services.repository.impl

import java.io.Serializable
import org.springframework.data.repository.PagingAndSortingRepository
import microon.services.repository.api.java.RepositoryService
import microon.spi.scala.activeobject.{Void, ActiveObject}
import org.springframework.data.domain.{Sort, Pageable}
import java.util.concurrent.Future
import java.lang.{Boolean, Long, Iterable}
import scala.collection.JavaConversions._

class DefaultJavaRepositoryService[T, ID <: Serializable]
(repository: PagingAndSortingRepository[T, ID], queryHandler: QueryHandler[T])
  extends RepositoryService[T, ID]
  with ActiveObject {

  def save[S <: T](entity: S): Future[S] = dispatch {
    repository.save(entity)
  }

  def saveMany[S <: T](entities: Iterable[S]): Future[Iterable[S]] = dispatch {
    repository.save(entities)
  }

  def findOne(id: ID): Future[T] = dispatch {
    repository.findOne(id)
  }

  def exists(id: ID): Future[Boolean] = dispatch {
    repository.exists(id)
  }

  def findAll(): Future[Iterable[T]] = dispatch {
    repository.findAll
  }

  def findAll(ids: Iterable[ID]): Future[Iterable[T]] = dispatch {
    repository.findAll(ids)
  }

  def count(): Future[Long] = dispatch {
    repository.count
  }

  def delete(id: ID): Future[Void] = void {
    repository.delete(id)
  }

  def delete(entity: T): Future[Void] = void {
    repository.delete(entity)
  }

  def delete(entities: Iterable[_ <: T]): Future[Void] = void {
    repository.delete(entities)
  }

  def deleteAll(): Future[Void] = void {
    repository.deleteAll()
  }

  def findAll(sort: Sort): Future[Iterable[T]] = dispatch {
    repository.findAll(sort)
  }

  def findAll(pageable: Pageable): Future[java.lang.Iterable[T]] = dispatch {
    repository.findAll(pageable)
  }

  def countByQuery(query: Any): Future[Long] = dispatch {
    queryHandler.countByQuery(query)
  }

  def findAllByQuery(query: Any): Future[Iterable[T]] = dispatch {
    queryHandler.findAllByQuery(query)
  }

  def findAllByQuery(query: Any, pageable: Pageable): Future[java.lang.Iterable[T]] = dispatch {
    queryHandler.findAllByQuery(query, pageable)
  }

  def findOneByQuery(query: Any): Future[T] = dispatch {
    queryHandler.findOneByQuery(query)
  }

}
