package microon.services.repository.impl

import org.springframework.data.domain.{Sort, Page, Pageable}

trait QueryHandler[T] {

  def countByQuery(query: Any): Long

  def findAllByQuery(query: Any): Seq[T]

  def findAllByQuery(query: Any, pageable: Pageable): Page[T]

  def findAllByQuery(query: Any, sort: Sort): Seq[T]

  def findOneByQuery(query: Any): T

}
