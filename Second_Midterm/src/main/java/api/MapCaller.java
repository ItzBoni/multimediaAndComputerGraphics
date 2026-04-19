package api;

public class MapCaller extends Connectable{
    ProcessBuilder command;
    private String apiKey;

    @Override
    public void setApiKey(String token){
        this.apiKey = token;
    }
}
