package microon.services.calendar.api.java;

import java.util.Date;

public class Event {

    private final String eventId;
    private final String summary;
    private final Date from;
    private final Date to;
    private final String[] attendees;

    public Event(String eventId, String summary, Date from, Date to, String[] attendees) {
        this.summary = summary;
        this.from = from;
        this.to = to;
        this.attendees = attendees;
        this.eventId = eventId;
    }

    public String eventId() {
        return eventId;
    }

    public String summary() {
        return summary;
    }

    public Date from() {
        return from;
    }

    public Date to() {
        return to;
    }

    public String[] attendees() {
        return attendees;
    }

}