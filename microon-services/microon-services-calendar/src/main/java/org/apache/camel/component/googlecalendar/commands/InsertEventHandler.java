package org.apache.camel.component.googlecalendar.commands;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import org.apache.camel.Exchange;
import org.apache.camel.component.googlecalendar.GoogleCalendarEndpoint;
import org.apache.camel.component.googlecalendar.GoogleCalendarProducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;

public class InsertEventHandler {

    public void handle(GoogleCalendarProducer producer, InsertEvent insertEventCommand, Exchange exchange) {
        Event event = new Event();
        event.setSummary(insertEventCommand.summary());

        EventDateTime from = new EventDateTime();
        from.setDateTime(new DateTime(insertEventCommand.from()));
        event.setStart(from);
        EventDateTime to = new EventDateTime();
        to.setDateTime(new DateTime(insertEventCommand.to()));
        event.setEnd(to);

        List<EventAttendee> eventAttendees = new ArrayList<EventAttendee>(insertEventCommand.attendees().size());
        for (String attendeeEmail : insertEventCommand.attendees()) {
            EventAttendee eventAttendee = new EventAttendee();
            eventAttendee.setEmail(attendeeEmail);
            eventAttendees.add(eventAttendee);
        }
        event.setAttendees(eventAttendees);

        try {
            GoogleCalendarEndpoint endpoint = producer.getEndpoint();
            endpoint.getCalendar().events().insert(endpoint.getCalendarId(), event).execute();
        } catch (IOException e) {
            throw wrapRuntimeCamelException(e);
        }
    }

}
