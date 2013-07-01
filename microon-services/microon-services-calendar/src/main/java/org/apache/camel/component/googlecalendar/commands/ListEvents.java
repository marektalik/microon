package org.apache.camel.component.googlecalendar.commands;

import java.util.Date;

public class ListEvents {

    private final Date from;

    public ListEvents(Date from) {
        this.from = from;
    }

    public Date from() {
        return from;
    }

}