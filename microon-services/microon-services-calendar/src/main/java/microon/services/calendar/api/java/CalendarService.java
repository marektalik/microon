package microon.services.calendar.api.java;

import java.util.Date;
import java.util.concurrent.Future;

public interface CalendarService {

    Future<Event[]> listEvents(Date from);

}
