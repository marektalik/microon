package microon.services.calendar.google

import microon.services.calendar.{Event, CalendarService}
import microon.spi.scala.activeobject.ActiveObject
import java.util.concurrent.Future
import org.apache.camel.CamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.component.googlecalendar.commands.ListEvents
import java.util.Date
import scala.collection.JavaConversions._
import org.apache.camel.component.googlecalendar

class CamelGoogleCalendarService(camelContext: CamelContext, calendarId: String, serviceAccountId: String, privateKeyFile: String)
  extends CalendarService with ActiveObject {

  // Endpoints

  val startEndpoint = "direct:microon-calendar"

  val calendarEndpoint = "google-calendar://%s?serviceAccountId=%s&privateKeyFile=%s".
    format(calendarId, serviceAccountId, privateKeyFile)

  // Route registration

  camelContext.addRoutes(new RouteBuilder {
    startEndpoint --> calendarEndpoint
  })

  // Operations

  def listEvents(from: Option[Date]): Future[Seq[Event]] = dispatch {
    val command = new ListEvents(from.getOrElse(null))
    camelContext.createProducerTemplate.requestBody(startEndpoint, command, classOf[java.util.List[googlecalendar.Event]]).map {
      event => new Event(event.summary, event.from, event.attendees)
    }
  }

}
