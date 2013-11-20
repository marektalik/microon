package microon.services.repository.impl

import org.springframework.data.domain.Pageable

trait QueryHandler[T] {

  def supports(query: Any): Boolean

  def countByQuery(query: Any): Long

  def findAllByQuery(query: Any): Seq[T]

  def findAllByQuery(query: Any, pageable: Pageable): Seq[T]

  def findOneByQuery(query: Any): T

}
