package api;

public class Connectable {
    //Adds required escapes to json so it doesn't break when sending it
    public String escapeJson(String s){
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    void setApiKey(String token){};
}
