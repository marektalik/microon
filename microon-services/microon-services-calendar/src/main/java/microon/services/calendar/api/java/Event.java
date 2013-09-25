package microon.services.calendar.api.java;

import java.util.Date;

public class Event {

    private final String summary;
    private final Date from;
    private final String[] attendees;

    public Event(String summary, Date from, String[] attendees) {
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

    public String[] attendees() {
        return attendees;
    }

}