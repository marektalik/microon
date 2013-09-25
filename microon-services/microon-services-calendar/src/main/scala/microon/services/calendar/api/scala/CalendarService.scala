package microon.services.calendar.api.scala

import java.util.concurrent.Future
import java.util.Date

trait CalendarService {

  def listEvents(from: Option[Date]): Future[Seq[Event]]

}
