package microon.services.repository.impl.queryhandler.jpa

import microon.services.repository.impl.QueryHandler
import org.springframework.data.domain.{Page, Pageable, Sort}
import com.mysema.query.types.Predicate
import com.mysema.query.jpa.impl.JPAQuery
import javax.persistence.EntityManager
import com.mysema.query.types.path.PathBuilder
import scala.collection.JavaConversions._
import scala.reflect.ClassTag
import scala.reflect.classTag

class JpaQueryDslQueryHandler[T: ClassTag](_entityType: Class[T], entityManager: EntityManager) extends QueryHandler[T] {

  def supports(query: Any): Boolean = {
    query.isInstanceOf[Predicate]
  }

  def countByQuery(query: Any): Long = {
    val predicate = query.asInstanceOf[Predicate]
    val jpaQuery = new JPAQuery(entityManager)
    val pathBuilder = new PathBuilder(classTag[T].runtimeClass.asInstanceOf[Class[T]], "entity")
    jpaQuery.from(pathBuilder).where(predicate).count
  }

  def findAllByQuery(query: Any): Seq[T] = {
    val predicate = query.asInstanceOf[Predicate]
    val jpaQuery = new JPAQuery(entityManager)
    val pathBuilder = new PathBuilder(classTag[T].runtimeClass.asInstanceOf[Class[T]], "entity")
    jpaQuery.from(pathBuilder).where(predicate).list(pathBuilder)
  }

  def findAllByQuery(query: Any, pageable: Pageable): Page[T] = null

  def findAllByQuery(query: Any, sort: Sort): Seq[T] = Seq.empty

  def findOneByQuery(query: Any): T = {
    val predicate = query.asInstanceOf[Predicate]
    val jpaQuery = new JPAQuery(entityManager)
    val pathBuilder = new PathBuilder(classTag[T].runtimeClass.asInstanceOf[Class[T]], "entity")
    jpaQuery.from(pathBuilder).where(predicate).uniqueResult(pathBuilder)
  }

}