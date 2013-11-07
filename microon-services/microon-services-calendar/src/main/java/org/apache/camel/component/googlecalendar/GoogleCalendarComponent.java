package org.apache.camel.component.googlecalendar;

import com.google.api.services.calendar.Calendar;
import org.apache.camel.impl.DefaultComponent;

import java.io.File;
import java.util.Map;

public class GoogleCalendarComponent extends DefaultComponent {

    private String calendarId;
    private String serviceAccountId;
    private File privateKeyFile;

    private Calendar calendar;

    @Override
    protected GoogleCalendarEndpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (calendar != null) {
            return new GoogleCalendarEndpoint(uri, this, calendar);
        }
        return new GoogleCalendarEndpoint(uri, this, resolveCalendarId(remaining), serviceAccountId, privateKeyFile);
    }

    private String resolveCalendarId(String remaining) {
        if (calendarId != null) {
            return calendarId;
        } else {
            return remaining;
        }
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getServiceAccountId() {
        return serviceAccountId;
    }

    public void setServiceAccountId(String serviceAccountId) {
        this.serviceAccountId = serviceAccountId;
    }

    public File getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(File privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}