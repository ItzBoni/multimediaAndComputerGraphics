package api;

import java.util.HashMap;
import java.util.Map;

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

    public String getDescription(String base64Image){
        String authHeader = "Authorization: Bearer " + apiKey.trim();

        //Building the body with the variables we receive as arguments
        String jsonBody = """
        {
            "model": "gpt-4o-mini",
            "messages": [
              {"role": "user", "content": "Generate a 3 second description of the following image: %s"}
            ]
        }""".formatted(base64Image);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey.trim());

        return RequestHandler.sendHttpRequest(
                "https://api.openai.com/v1/chat/completions",
                "POST",
                jsonBody,
                headers
        );
    }
}
