package microon.services.calendar.api.scala

import java.util.Date

case class Event(summary: String, from: Date, attendees: Seq[String])