package microon.services.calendar.google

import org.apache.camel.CamelContext
import org.apache.camel.component.googlecalendar.commands.{ListEvents => CamelListEvents}
import java.util.Date
import scala.collection.JavaConversions._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}
import org.mockito.BDDMockito._
import Matchers._
import Matchers.{eq => eql}
import org.springframework.scala.context.function.{FunctionalConfigApplicationContext, ContextSupport, FunctionalConfiguration}
import java.util.concurrent.Executors._
import microon.services.calendar.Event
import org.apache.camel.component.googlecalendar.{Event => CamelEvent}
import java.util.{List => JavaList}
import microon.ri.activeobject.ExecutorServiceActiveObjectDispatcher

class CamelGoogleCalendarServiceTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  // Data fixtures

  val camelEvent = new CamelEvent("summary", new Date, List("attendee"))

  // Collaborators fixtures

  var service: CamelGoogleCalendarService = _
  var camelContext: CamelContext = _

  before {
    val context = FunctionalConfigApplicationContext[TestConfig]
    camelContext = context.bean[CamelContext].get
    service = context.bean[CamelGoogleCalendarService].get
  }

  // Tests

  test("Should convert Camel events to Microon events") {
    given(
      camelContext.createProducerTemplate.requestBody(anyString, any[CamelListEvents], eql(classOf[JavaList[CamelEvent]]))
    ).willReturn(List(camelEvent))
    val expectedMicroonEvent = Event(camelEvent.summary, camelEvent.from, camelEvent.attendees)

    expectResult(Seq(expectedMicroonEvent)) {
      service.listEvents(from = None).get
    }
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport with MockitoSugar {
  enableAnnotationConfig()

  val executor = bean()(newCachedThreadPool)
  bean()(new ExecutorServiceActiveObjectDispatcher(executor()))

  val camelContext = bean()(mock[CamelContext](Mockito.RETURNS_DEEP_STUBS))
  bean()(new CamelGoogleCalendarService(camelContext(), "calendarId", "serviceId", "secretPath"))
}