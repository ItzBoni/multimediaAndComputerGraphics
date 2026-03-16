import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class AICommunicator {
    private String apiKey;

    //Gets the token using encapsulation
    public AICommunicator(){
        setApiKey(System.getenv("OpenAIToken"));
    }

    private void setApiKey(String token){
        this.apiKey = token;
    }

    //Adds required escapes to json so it doesn't break when sending it
    private String escapeJson(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    //Gets translation from OpenAI
    public String getTranslation(String originalText, String lang){
        String safeText = escapeJson(originalText);
        String safeLang = escapeJson(lang);
        String authHeader = "Authorization: Bearer " + apiKey.trim();
        StringBuilder response = new StringBuilder();

        try {
            //Building the body with the variables we receive as arguments
            String jsonBody = """
               {
                   "model": "gpt-4o-mini",
                   "messages": [
                     {"role": "user", "content": "Please translate the following text into %s and only output the translation: %s"}
                   ]
                 }""".formatted(lang, safeText);

            // write JSON to temp file
            Path temp = Files.createTempFile("openai", ".json");
            Files.writeString(temp, jsonBody);

            //Creates the ProcessBuilder directly with the command
            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-s",
                    "-X", "POST",
                    "https://api.openai.com/v1/chat/completions",
                    "-H", "Content-Type: application/json",
                    "-H", "Authorization: Bearer " + apiKey,
                    "--data-binary", "@" + temp.toAbsolutePath()
            );


            pb.redirectErrorStream(true);//Redirects the errors to console.
            final Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            process.waitFor();
            Files.deleteIfExists(temp);
            process.destroy();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Code below converts the response into a String and formats to return a valid string to write into file
        String fullResponse = response.toString();
        int messageIndex = fullResponse.indexOf("\"message\"");
        String marker = "\"content\": \"";

        int start = fullResponse.indexOf(marker, messageIndex) + marker.length();

        int end = start;
        while (true) {
            end = fullResponse.indexOf("\"", end);
            if (fullResponse.charAt(end - 1) != '\\') {
                break; // found a real closing quote
            }
            end++; // skip escaped quote
        }
        String translation = fullResponse.substring(start, end);
        translation = translation
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");

        return translation;
    }
}
