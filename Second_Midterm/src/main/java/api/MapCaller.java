package api;

public class MapCaller extends Connectable{
    ProcessBuilder command;
    private String apiKey;


    public void setApiKey(String token){
        this.apiKey = token;
    }
}
