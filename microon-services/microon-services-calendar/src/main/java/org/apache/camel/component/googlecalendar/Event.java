package org.apache.camel.component.googlecalendar;

import java.util.Date;
import java.util.List;

public class Event {

    private final String summary;
    private final Date from;
    private final List<String> attendees;

    public Event(String summary, Date from, List<String> attendees) {
        this.summary = summary;
        this.from = from;
        this.attendees = attendees;
    }


    public String summary() {
        return summary;
    }

    public Date from() {
        return from;
    }

    public List<String> attendees() {
        return attendees;
    }
}
