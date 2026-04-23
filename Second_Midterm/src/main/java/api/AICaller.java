package api;

import handlers.RequestHandler;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for OpenAI API endpoints.
 *
 * Provides methods for:
 * - Analyzing images with GPT-4 Vision
 * - Merging multiple descriptions into unified text
 * - Converting text to speech
 * - Generating images from prompts
 *
 * Loads API key from .env file at initialization.
 */
public class AICaller extends Connectable{
    private String apiKey;

    /**
     * Initializes AICaller and loads OpenAI API key from environment.
     * Reads OPENAI_API_KEY from .env file.
     */
    public AICaller(){
        Dotenv dotenv = Dotenv.load();
        setApiKey(dotenv.get("OPENAI_API_KEY"));
    }

    /**
     * Sets the OpenAI API key for authentication.
     *
     * @param token the API key to use for requests
     */
    public void setApiKey(String token){
        this.apiKey = token;
    }

    /**
     * Merges multiple media descriptions into a single unified narrative.
     *
     * Uses GPT-4 mini to create coherent text from multiple disparate descriptions.
     *
     * @param descriptions concatenated descriptions from multiple media items
     * @return unified description suitable for narration
     */
    public String mergeDescriptions(String descriptions){
        String safeDescriptions = escapeJson(descriptions);

        String jsonBody = """
        {
           "model": "gpt-4o-mini",
           "messages": [
             {"role": "user", "content": "Generate a unified description based on the following text. No notes, only text.: %s"}
           ]
         }""".formatted(safeDescriptions);

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


    /**
     * Generates a descriptive text for an image using GPT-4 Vision.
     *
     * Prompts the AI to write 1-2 fluent spoken sentences describing the image
     * in present tense, as if narrating a slideshow segment.
     *
     * @param base64Image base64-encoded image data
     * @return AI-generated description (1-2 sentences)
     */
    public String descriptionRequest(String base64Image){
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

    /**
     * Converts text to speech audio using OpenAI's text-to-speech engine.
     *
     * @param description text to convert to audio
     * @return raw MP3 audio bytes
     */
    public byte[] audioRequest(String description){
        String safeDescription = escapeJson(description);

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

    /**
     * Generates an image based on a text description using DALL-E 3.
     *
     * Creates a 1024x1536 image at low quality for faster generation.
     *
     * @param description text prompt describing the desired image
     * @return base64-encoded PNG image data
     */
    public String imageRequest(String description){
        String safeDescription = escapeJson(description);

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

    /**
     * Parses OpenAI API response to extract content data.
     *
     * Handles two response formats:
     * - Base64-encoded binary data (images, audio)
     * - Text content (descriptions)
     *
     * Unescapes special characters in text responses.
     *
     * @param apiResponse raw JSON response from OpenAI API
     * @return extracted content (base64 data or text string)
     */
    private static String parseResponse(String apiResponse) {
        String b64Marker = "\"b64_json\": \"";
        int b64Index = apiResponse.indexOf(b64Marker);
        if (b64Index != -1) {
            int start = b64Index + b64Marker.length();
            int end = apiResponse.indexOf("\"", start);
            return apiResponse.substring(start, end);
        }
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
