package org.apache.camel.component.googlecalendar;

import java.util.Date;
import java.util.List;

public class Event {

    private final String eventId;
    private final String summary;
    private final Date from;
    private final Date to;
    private final List<String> attendees;

    public Event(String eventId, String summary, Date from, Date to, List<String> attendees) {
        this.eventId = eventId;
        this.summary = summary;
        this.from = from;
        this.to = to;
        this.attendees = attendees;
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


    public List<String> attendees() {
        return attendees;
    }

    public String eventId() {
        return eventId;
    }

}