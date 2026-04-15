package api;

public class MapCaller implements Connectable{
    ProcessBuilder command;
    private String apiKey;


    @Override
    public String escapeJson(String s){
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public void setApiKey(String token){
        this.apiKey = token;
    }
}
