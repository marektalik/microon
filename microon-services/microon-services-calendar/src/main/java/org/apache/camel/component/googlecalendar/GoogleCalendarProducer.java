package org.apache.camel.component.googlecalendar;

import org.apache.camel.Exchange;
import org.apache.camel.component.googlecalendar.commands.ListEvents;
import org.apache.camel.component.googlecalendar.commands.ListEventsHandler;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class GoogleCalendarProducer extends DefaultProducer {

    // Constants

    private final static Logger LOG = getLogger(GoogleCalendarProducer.class);

    // Constructors

    public GoogleCalendarProducer(GoogleCalendarEndpoint endpoint) {
        super(endpoint);
    }

    // Overridden methods

    @Override
    public void process(Exchange exchange) throws Exception {
        Object command = exchange.getIn().getBody();
        if (command == null) {
            LOG.warn("Empty command sent to the GoogleCalendar producer endpoint.");
        } else if (command instanceof ListEvents) {
            ListEvents listEventsCommand = (ListEvents) command;
            new ListEventsHandler().handle(this, listEventsCommand, exchange);
        }
    }

    @Override
    public GoogleCalendarEndpoint getEndpoint() {
        return (GoogleCalendarEndpoint) super.getEndpoint();
    }

}
