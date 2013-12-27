package microon.services.audit.api.java;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuditEvent implements Serializable{

    private String id;

    private String message;

    private Map<String, String> context = new HashMap<String, String>();

    private String[] tags = new String[0];

    public AuditEvent() {
    }

    public AuditEvent(String id, String message, Map<String, String> context, String[] tags) {
        this.id = id;
        this.message = message;
        this.context = context;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

}
