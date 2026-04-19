package handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Map;

public class RequestHandler {

    public static String sendHttpRequest(String url, String method, String jsonBody, Map<String, String> headers) {
        return send(url, method, jsonBody, headers, HttpResponse.BodyHandlers.ofString());
    }

    public static byte[] sendHttpRequestBytes(String url, String method, String jsonBody, Map<String, String> headers) {
        return send(url, method, jsonBody, headers, HttpResponse.BodyHandlers.ofByteArray());
    }

    //Use <T> to let Java decide what response format to send. Works in this use case.
    private static <T> T send(String url, String method, String jsonBody, Map<String, String> headers,
                              HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }

            if ("POST".equalsIgnoreCase(method)) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
            } else if ("GET".equalsIgnoreCase(method)) {
                requestBuilder.GET();
            } else {
                throw new IllegalArgumentException("Unsupported method: " + method);
            }

            HttpResponse<T> httpResponse = client.send(requestBuilder.build(), bodyHandler);
            System.out.println("Status Code: " + httpResponse.statusCode());
            return httpResponse.body();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}