package org.apache.camel.component.googlecalendar;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.googlecalendar.commands.InsertEvent;
import org.apache.camel.component.googlecalendar.commands.ListEvents;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

public class GoogleCalendarComponentTestIT extends CamelTestSupport {

    // Test data fixtures

    String calendarId = "ab30iq6rvk16k5cent3jun4jrk@group.calendar.google.com";
    String serviceAccountId = "551495289515@developer.gserviceaccount.com";
    String privateKeyFile = "/home/hekonsek/Desktop/157851520163832d99534dc2e9dde5ff3979eb5e-privatekey.p12";

    String firstParticipantEmail = "firstParticipantEmail@foo.com";
    String secondParticipantEmail = "secondParticipantEmail@foo.com";

    @Override
    protected void doPostSetup() throws Exception {
        super.doPostSetup();
        sendBody("direct:test", new InsertEvent("Event 1", new Date(), new Date(), singletonList(firstParticipantEmail)));
        sendBody("direct:test", new InsertEvent("Event 2", new Date(), new Date(), singletonList(secondParticipantEmail)));
    }

    // Routing fixtures

    String startTest = "direct:test";
    String mock = "mock:test";
    String calendarUri = String.format("google-calendar://%s?serviceAccountId=%s&privateKeyFile=%s",
            calendarId,
            serviceAccountId,
            privateKeyFile);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(startTest).to(calendarUri).to(mock);
            }
        };
    }

    // Tests

    @Test
    @Ignore
    public void shouldListEventsWithAttendeeEmail() {
        // Given
        getMockEndpoint(mock).reset();

        // When
        sendBody(startTest, new ListEvents(null));

        // Then
        Exchange exchangeReceived = getMockEndpoint(mock).getExchanges().get(0);
        @SuppressWarnings("unchecked")
        List<Event> eventsReceived = exchangeReceived.getIn().getBody(List.class);
        List<Event> eventsWithParticipant = new LinkedList<Event>();
        for (Event event : eventsReceived) {
            if (event.attendees().contains(firstParticipantEmail.toLowerCase())) {
                eventsWithParticipant.add(event);
            }
        }
        assertEquals(1, eventsWithParticipant.size());
    }

}