package api;

public class AICaller extends Connectable{
    private String apiKey;

    //Gets the token using encapsulation
    public AICaller(){
        setApiKey(System.getenv("OpenAIToken"));
    }

    @Override
    public void setApiKey(String token){
        this.apiKey = token;
    }
}
