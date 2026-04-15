package api;

public class AICaller implements Connectable{
    private String apiKey;

    //Gets the token using encapsulation
    public AICaller(){
        setApiKey(System.getenv("OpenAIToken"));
    }

    private void setApiKey(String token){
        this.apiKey = token;
    }
}
