package org.apache.camel.component.googlecalendar.commands;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventAttendee;
import org.apache.camel.Exchange;
import org.apache.camel.component.googlecalendar.Event;
import org.apache.camel.component.googlecalendar.GoogleCalendarProducer;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;

public class ListEventsHandler {

    public void handle(GoogleCalendarProducer producer, ListEvents listEventsCommand, Exchange exchange) {
        try {
            Calendar calendar = producer.getEndpoint().getCalendar();
            Calendar.Events.List eventsQuery = calendar.events().list(producer.getEndpoint().getCalendarId());
            if (listEventsCommand.from() != null) {
                eventsQuery.setTimeMin(new DateTime(listEventsCommand.from()));
            }

            List<Event> camelEvents = new LinkedList<Event>();
            for (com.google.api.services.calendar.model.Event event : eventsQuery.execute().getItems()) {
                String summary = event.getSummary();
                Date from = new Date(event.getStart().getDateTime().getValue());
                List<String> attendees = new LinkedList<String>();
                if (event.getAttendees() != null) {
                    for (EventAttendee attendee : event.getAttendees()) {
                        attendees.add(attendee.getEmail());
                    }
                }
                camelEvents.add(new org.apache.camel.component.googlecalendar.Event(summary, from, attendees));
            }
            exchange.getOut().setBody(camelEvents);
        } catch (IOException e) {
            throw wrapRuntimeCamelException(e);
        }
    }

}
