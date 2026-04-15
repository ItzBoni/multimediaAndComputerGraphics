package api;

public interface Connectable {
    //Adds required escapes to json so it doesn't break when sending it
    String escapeJson(String s);

    void setApiKey(String token);
}
