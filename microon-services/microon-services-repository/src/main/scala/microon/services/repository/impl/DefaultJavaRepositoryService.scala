package microon.services.repository.impl

import java.io.Serializable
import org.springframework.data.repository.PagingAndSortingRepository
import microon.services.repository.api.java.RepositoryService
import microon.spi.scala.activeobject.{Void, ActiveObject}
import org.springframework.data.domain.{Sort, Page, Pageable}
import java.util.concurrent.Future
import java.lang.{Boolean, Long, Iterable}

class DefaultJavaRepositoryService[T, ID <: Serializable](repository: PagingAndSortingRepository[T, ID])
  extends RepositoryService[T, ID]
  with ActiveObject {

  def save[S <: T](entity: S): Future[S] = dispatch {
    repository.save(entity)
  }

  def save[S <: T](entities: Iterable[S]): Future[Iterable[S]] = dispatch {
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

  def findAll(pageable: Pageable): Future[Page[T]] = dispatch {
    repository.findAll(pageable)
  }

}
