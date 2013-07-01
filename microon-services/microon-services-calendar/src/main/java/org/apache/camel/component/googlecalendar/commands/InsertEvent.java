package org.apache.camel.component.googlecalendar.commands;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.apache.camel.util.ObjectHelper.notNull;

public class InsertEvent {

    private final String summary;
    private final Date from;
    private final Date to;
    private final List<String> attendees;

    public InsertEvent(String summary, Date startDate, Date endDate, List<String> attendees) {
        this.summary = (String) notNull(summary, "summary");
        this.from = (Date) notNull(startDate, "start date");
        this.to = (Date) notNull(endDate, "end date");
        this.attendees = attendees != null ? attendees : Collections.<String>emptyList();
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

}