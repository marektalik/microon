package microon.services.calendar.google

import microon.services.calendar.{Event, CalendarService}
import microon.spi.scala.activeobject.ActiveObject
import java.util.concurrent.Future
import org.apache.camel.CamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.component.googlecalendar.commands.ListEvents
import java.util.Date
import scala.collection.JavaConversions._

class GoogleCalendarService(camelContext: CamelContext, calendarId: String, serviceAccountId: String, privateKeyFile: String)
  extends CalendarService with ActiveObject {

  camelContext.addRoutes(new RouteBuilder {
    "direct:microon-calendar" --> "google-calendar://%s?serviceAccountId=%s&privateKeyFile=%s".
      format(calendarId, serviceAccountId, privateKeyFile)
  })

  def listEvents(from: Option[Date]): Future[Seq[Event]] = dispatch {
    camelContext.createProducerTemplate.requestBody("direct:microon-calendar", new ListEvents(from.getOrElse(null)), classOf[java.util.List[Event]])
  }

}
