package api;

import handlers.RequestHandler;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

public class AICaller extends Connectable{
    private String apiKey;

    public AICaller(){
        Dotenv dotenv = Dotenv.load();
        setApiKey(dotenv.get("OPENAI_API_KEY"));
    }

    public void setApiKey(String token){
        this.apiKey = token;
    }

    public String descriptionRequest(String base64Image){
        //Building the body with the variables we receive as arguments
        String jsonBody = """
        {
            "model": "gpt-4o-mini",
            "messages": [
              {
                "role": "user",
                "content": [
                  {
                    "type": "image_url",
                    "image_url": {
                      "url": "data:image/jpeg;base64,%s",
                      "detail": "low"
                    }
                  },
                  {
                    "type": "text",
                    "text": "You are narrating a photo slideshow segment. Write 1-2 fluent, spoken sentences describing this image. Use present tense, vivid but brief language. Write as if this is one moment in a continuous story — no opening greetings, no closing remarks, just the narration."
                  }
                ]
              }
            ],
            "max_tokens": 100
        }""".formatted(base64Image);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey.trim());

        String apiResponse =  RequestHandler.sendHttpRequest(
                "https://api.openai.com/v1/chat/completions",
                "POST",
                jsonBody,
                headers
        );

        return parseResponse(apiResponse);
    }

    public byte[] audioRequest(String description){
        String safeDescription = escapeJson(description);

        //Building the body with the variables we receive as arguments
        String jsonBody = """
        {
          "model": "gpt-4o-mini-tts",
          "input": "%s",
          "voice": "alloy"
        }""".formatted(safeDescription);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey.trim());

        return RequestHandler.sendHttpRequestBytes(
                "https://api.openai.com/v1/audio/speech",
                "POST",
                jsonBody,
                headers
        );
    }

    public String imageRequest(String description){
        String safeDescription = escapeJson(description);

        //Building the body with the variables we receive as arguments
        String jsonBody = """
            {
              "model": "gpt-image-1-mini",
              "prompt": "%s",
              "size": "1024x1536",
              "quality": "low",
              "n": 1
            }""".formatted(safeDescription);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + apiKey.trim());

        String response =  RequestHandler.sendHttpRequest(
                "https://api.openai.com/v1/images/generations",
                "POST",
                jsonBody,
                headers
        );

        return parseResponse(response);
    }

    private static String parseResponse(String apiResponse) {
        String b64Marker = "\"b64_json\": \"";
        int b64Index = apiResponse.indexOf(b64Marker);
        if (b64Index != -1) {
            int start = b64Index + b64Marker.length();
            int end = apiResponse.indexOf("\"", start);
            return apiResponse.substring(start, end);
        }

        // Fall back to text content parsing
        int messageIndex = apiResponse.indexOf("\"message\"");
        String marker = "\"content\": \"";

        int start = apiResponse.indexOf(marker, messageIndex) + marker.length();

        int end = start;
        while (true) {
            end = apiResponse.indexOf("\"", end);
            if (apiResponse.charAt(end - 1) != '\\') {
                break;
            }
            end++;
        }
        String finalResponse = apiResponse.substring(start, end);
        finalResponse = finalResponse
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
        return finalResponse;
    }
}
