package api;

public interface Connectable {
    //Adds required escapes to json so it doesn't break when sending it
    private String escapeJson(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void setApiKey(String token){}
}
