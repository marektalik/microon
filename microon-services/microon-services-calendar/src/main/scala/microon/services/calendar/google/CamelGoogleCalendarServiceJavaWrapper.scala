package microon.services.calendar.google

import microon.services.calendar.api.java.{CalendarService => JavaCalendarService, Event => JavaEvent}
import java.util.Date
import java.util.concurrent.Future
import microon.services.calendar.api.scala.CalendarService
import com.google.common.util.concurrent.Futures.immediateFuture

class CamelGoogleCalendarServiceJavaWrapper(calendarService: CalendarService) extends JavaCalendarService {

  def listEvents(from: Date): Future[Array[JavaEvent]] =
    immediateFuture(calendarService.listEvents(Option(from)).get().map(event => new JavaEvent(event.eventId, event.summary, event.from, event.attendees.toArray)).toArray)

}