package microon.services.repository.impl.queryhandler.jpa

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import scala.beans.BeanProperty
import javax.persistence.{Entity, GeneratedValue, Id}
import com.mysema.query.types._
import scalapi.querydsl.QueryDslPredicates._
import scalapi.hibernate.Hibernates.defaultEntityManagerFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JpaQueryDslQueryHandlerTest extends FunSuite with BeforeAndAfterAll {

  // Collaborators fixtures

  val entityManager = defaultEntityManagerFactory().addAnnotatedType[TestUser].build.createEntityManager

  val handler = new JpaQueryDslQueryHandler(classOf[TestUser], entityManager)

  // Data fixtures

  val user = new TestUser("someName", "someSurname")

  val query = predicate[TestUser]("name", Ops.EQ, user.name)

  override protected def beforeAll() {
    val transaction = entityManager.getTransaction
    transaction.begin()
    entityManager.persist(user)
    transaction.commit()
  }

  // Tests

  test("Should support query.") {
    assert(true === handler.supports(query))
  }

  test("Should not support query.") {
    assert(false === handler.supports("invalid query"))
  }

  test("Should find by query.") {
    assert(user === handler.findAllByQuery(query).head)
  }

  test("Should not find by query.") {
    expectResult(true) {
      val query = predicate[TestUser]("name", Ops.EQ, "invalid name")
      handler.findAllByQuery(query).isEmpty
    }
  }

  test("Should count by query.") {
    assert(1 === handler.countByQuery(query))
  }

  test("Should find one by query.") {
    assert(user === handler.findOneByQuery(query))
  }

}

@Entity(name = "TestUser")
class TestUser
(@BeanProperty var name: String = null,
 @BeanProperty var surname: String = null) {
  @Id
  @GeneratedValue var id: Long = _
}

