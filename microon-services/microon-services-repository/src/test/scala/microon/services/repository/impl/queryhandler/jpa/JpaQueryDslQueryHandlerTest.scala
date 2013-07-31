package microon.services.repository.impl.queryhandler.jpa

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.hibernate.ejb.Ejb3Configuration
import scala.beans.BeanProperty
import javax.persistence.{Entity, GeneratedValue, Id}
import java.util.Properties
import org.hibernate.cfg.AvailableSettings
import com.mysema.query.types._
import com.mysema.query.support.Expressions
import com.mysema.query.types.path.PathBuilder
import scala.reflect.ClassTag
import scala.reflect.classTag
import QueryDslPredicates.predicate

class JpaQueryDslQueryHandlerTest extends FunSuite with BeforeAndAfterAll {

  // Collaborators fixtures

  val hibernateProperties = new Properties()
  hibernateProperties.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect")
  hibernateProperties.setProperty(AvailableSettings.DRIVER, "org.hsqldb.jdbcDriver")
  hibernateProperties.setProperty(AvailableSettings.URL, "jdbc:hsqldb:mem:testdb")
  hibernateProperties.setProperty(AvailableSettings.USER, "sa")
  hibernateProperties.setProperty(AvailableSettings.PASS, "")
  hibernateProperties.setProperty(AvailableSettings.SHOW_SQL, "true")
  hibernateProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "update")

  val entityManager = new Ejb3Configuration().addProperties(hibernateProperties).addAnnotatedClass(classOf[TestUser]).buildEntityManagerFactory.createEntityManager

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

object QueryDslPredicates {

  val entityVariable = "entity"

  def predicate[E: ClassTag](property: String, operator: Operator[java.lang.Boolean], value: AnyRef): Predicate = {
    val entityPath = new PathBuilder(classTag[E].runtimeClass, entityVariable)
    val propertyPath = Expressions.path(value.getClass, entityPath, property)
    val valueExpression = Expressions.constant(value)
    Expressions.predicate(operator, propertyPath, valueExpression)
  }

}