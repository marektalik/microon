package org.apache.camel.component.googlecalendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;
import static java.util.Collections.singletonList;
import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;
import static org.slf4j.LoggerFactory.getLogger;

public class GoogleCalendarEndpoint extends DefaultEndpoint {

    private final static Logger LOG = getLogger(GoogleCalendarEndpoint.class);
    private final JsonFactory JSON_FACTORY = new JacksonFactory();
    private HttpTransport httpTransport;
    private String calendarId;
    private String serviceAccountId;
    private File privateKeyFile;
    private Calendar calendar;

    protected GoogleCalendarEndpoint(String endpointUri, GoogleCalendarComponent component,
                                     String calendarId, String serviceAccountId, File privateKeyFile) {
        super(endpointUri, component);
        this.calendarId = calendarId;
        this.serviceAccountId = serviceAccountId;
        this.privateKeyFile = privateKeyFile;

        try {
            httpTransport = newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw wrapRuntimeCamelException(e);
        } catch (IOException e) {
            throw wrapRuntimeCamelException(e);
        }
    }

    protected GoogleCalendarEndpoint(String endpointUri, GoogleCalendarComponent component, Calendar calendar) {
        super(endpointUri, component);
        this.calendar = calendar;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        LOG.debug("Building Google Credentials for calendar: {}", calendarId);
        if (calendar == null) {
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(serviceAccountId)
                    .setServiceAccountScopes(singletonList(CalendarScopes.CALENDAR))
                    .setServiceAccountPrivateKeyFromP12File(privateKeyFile)
                    .build();
            calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential).build();
        }
    }

    @Override
    public Producer createProducer() throws Exception {
        return new GoogleCalendarProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Google Calendar component doesn't support consumer endpoints.");
    }

    @Override
    public boolean isSingleton() {
        return true;
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