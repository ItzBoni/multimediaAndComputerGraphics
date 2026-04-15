package api;

public class AICaller implements Connectable{
    private String apiKey;

    //Gets the token using encapsulation
    public AICaller(){
        setApiKey(System.getenv("OpenAIToken"));
    }

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
