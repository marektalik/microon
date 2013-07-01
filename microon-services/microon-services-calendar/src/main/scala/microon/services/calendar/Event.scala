package microon.services.calendar

import java.util.Date

case class Event(summary: String, from: Date, attendees: Seq[String])
