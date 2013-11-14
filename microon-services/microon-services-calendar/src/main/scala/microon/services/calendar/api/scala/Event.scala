package microon.services.calendar.api.scala

import java.util.Date

case class Event(eventId: String, summary: String, from: Date, to: Date, attendees: Seq[String])
