package microon.services.repository.impl.queryhandler.springdatamongoquery

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.beans.BeanProperty
import scalapi.embedmongo.EmbedMongoSupport
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.{Criteria, Query}
import org.springframework.data.domain.PageRequest
import org.springframework.scala.context.function.{FunctionalConfiguration, FunctionalConfigApplicationContext}

@RunWith(classOf[JUnitRunner])
class SpringDataMongoQueryQueryHandlerTest extends FunSuite {

  // Context fixture

  val applicationContext = FunctionalConfigApplicationContext[TestConfig]()

  // Test subject fixture

  val handler = applicationContext[SpringDataMongoQueryQueryHandler[TestUser]]

  // Data fixtures

  val mongoTemplate = applicationContext[MongoTemplate]

  val fred = new TestUser(null, "Fred", "Flinstone")
  mongoTemplate.save(fred)

  val willma = new TestUser(null, "Willma", "Flinstone")
  mongoTemplate.save(willma)

  // Tests

  test("Should count Fred.") {
    assertResult(1) {
      handler.countByQuery(Query.query(Criteria.where("name").is(fred.getName)))
    }
  }

  test("Should count Willma.") {
    assertResult(1) {
      handler.countByQuery(Query.query(Criteria.where("name").is(willma.getName)))
    }
  }

  test("Should find Willma.") {
    assertResult(Seq(willma.getName)) {
      handler.findAllByQuery(Query.query(Criteria.where("name").is(willma.getName))).map(_.getName)
    }
  }

  test("Should find first page.") {
    assertResult(Seq(fred.getName)) {
      handler.findAllByQuery(new Query(), new PageRequest(0, 1)).map(_.getName)
    }
  }

  test("Should find second page.") {
    assertResult(Seq(willma.getName)) {
      handler.findAllByQuery(new Query(), new PageRequest(1, 1)).map(_.getName)
    }
  }

  test("Should find only Fred.") {
    assertResult(fred.getName) {
      handler.findOneByQuery(Query.query(Criteria.where("name").is(fred.getName))).getName
    }
  }

}

class TestUser(@BeanProperty var id: String,
               @BeanProperty var name: String,
               @BeanProperty var surname: String)

class TestConfig extends FunctionalConfiguration with EmbedMongoSupport {

  val mongoTemplate = bean() {
    new MongoTemplate(mongo, "testdb")
  }

  bean() {
    SpringDataMongoQueryQueryHandler[TestUser](mongoTemplate())
  }

}