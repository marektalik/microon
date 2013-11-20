package microon.services.repository.impl.queryhandler.springdatamongoquery

import microon.services.repository.impl.QueryHandler
import scala.reflect._
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.MongoTemplate
import scala.collection.JavaConversions._

class SpringDataMongoQueryQueryHandler[T: ClassTag](_entityType: Class[T], mongoTemplate: MongoTemplate) extends QueryHandler[T] {

  def supports(query: Any): Boolean =
    query.isInstanceOf[Query]

  def countByQuery(query: Any): Long = {
    val predicate = query.asInstanceOf[Query]
    mongoTemplate.count(predicate, _entityType)
  }

  def findAllByQuery(query: Any): Seq[T] = {
    val predicate = query.asInstanceOf[Query]
    mongoTemplate.find(predicate, _entityType).toList
  }

  def findAllByQuery(query: Any, pageable: Pageable): Seq[T] = {
    val predicate = query.asInstanceOf[Query]
    mongoTemplate.find(predicate.`with`(pageable), _entityType).toList
  }

  def findOneByQuery(query: Any): T = {
    val predicate = query.asInstanceOf[Query]
    mongoTemplate.findOne(predicate, _entityType)
  }

}