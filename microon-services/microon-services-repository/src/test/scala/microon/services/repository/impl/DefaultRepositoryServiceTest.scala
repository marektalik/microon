package microon.services.repository.impl

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import microon.ri.boot.spring.scala.SpringScalaBoot
import org.springframework.data.repository.PagingAndSortingRepository
import org.mockito.Mockito._
import microon.services.repository.api.scala.RepositoryService
import scala.collection.JavaConversions._

class DefaultRepositoryServiceTest extends FunSuite with BeforeAndAfter {

  val id = "id"
  val foo = Foo(id)

  val boot = SpringScalaBoot[TestConfig].start()
  val service = boot.context[RepositoryService[Foo, String]]
  val repository = boot.context[PagingAndSortingRepository[Foo, String]]

  before {
    reset(repository)
  }

  test("Should save entity.") {
    service.save(foo).get
    verify(repository).save(foo)
  }

  test("Should save entities.") {
    val entities = Seq(foo, foo)
    service.save(entities).get
    val entitiesIterator: java.lang.Iterable[Foo] = entities.toList
    verify(repository).save(entitiesIterator)
  }

  test("Should find entity.") {
    service.findOne(id).get
    verify(repository).findOne(id)
  }

  test("Should verify entity existence.") {
    service.exists(id).get
    verify(repository).exists(id)
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val repository = bean() {
    mock(classOf[PagingAndSortingRepository[Foo, String]], RETURNS_DEEP_STUBS)
  }

  bean() {
    new DefaultRepositoryService(repository())
  }

}

case class Foo(id: String)