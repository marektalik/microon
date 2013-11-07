package org.apache.camel.component.googlecalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.googlecalendar.commands.ListEvents;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class GoogleCalendarProducerTest extends CamelTestSupport {

    // Collaborators fixture

    Calendar calendar = mock(Calendar.class, RETURNS_DEEP_STUBS);

    // Test data fixtures

    com.google.api.services.calendar.model.Event googleEvent;

    Date eventFrom = new Date();

    Date eventTo = new Date();

    @Override
    protected void doPreSetup() throws Exception {
        super.doPreSetup();
        googleEvent = new com.google.api.services.calendar.model.Event();
        googleEvent.setId("googleEventId");
        googleEvent.setSummary("summary");
        EventDateTime start = new EventDateTime();
        start.setDateTime(new DateTime(eventFrom));
        googleEvent.setStart(start);
        EventDateTime end = new EventDateTime();
        end.setDateTime(new DateTime(eventTo));
        googleEvent.setEnd(end);

        Events events = new Events();
        events.setItems(singletonList(googleEvent));
        given(calendar.events().list(anyString()).execute()).
                willReturn(events);
    }

    // Routing fixtures

    @EndpointInject(uri = "mock:test")
    MockEndpoint mockEndpoint;

    String startTest = "direct:test";
    String calendarComponentName = "mocked-google-calendar";
    String calendarUri = calendarComponentName + ":///";

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        GoogleCalendarComponent googleCalendarComponent = new GoogleCalendarComponent();
        googleCalendarComponent.setCalendar(calendar);
        camelContext.addComponent(calendarComponentName, googleCalendarComponent);
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(startTest).to(calendarUri).to("mock:test");
            }
        };
    }

    // Tests

    @Test
    public void shouldFetchGoogleEventId() throws IOException {
        // When
        sendBody(startTest, new ListEvents(null));

        // Then
        Exchange exchangeReceived = mockEndpoint.getExchanges().get(0);
        @SuppressWarnings("unchecked")
        List<Event> eventsReceived = exchangeReceived.getIn().getBody(List.class);
        assertEquals(1, eventsReceived.size());
        assertEquals(googleEvent.getId(), eventsReceived.get(0).eventId());
    }

    @Test
    public void shouldFetchGoogleEventToDate() throws IOException, InterruptedException {
        // When
        sendBody(startTest, new ListEvents(null));

        // Then
        Exchange exchangeReceived = mockEndpoint.getExchanges().get(0);
        @SuppressWarnings("unchecked")
        List<Event> eventsReceived = exchangeReceived.getIn().getBody(List.class);
        assertEquals(1, eventsReceived.size());
        assertEquals(googleEvent.getEnd().getDateTime().getValue(), eventsReceived.get(0).to().getTime());
    }

}