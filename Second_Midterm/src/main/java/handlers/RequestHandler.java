package handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Handles HTTP request/response communication with REST APIs.
 *
 * Provides methods for:
 * - Sending text-based HTTP requests (JSON responses)
 * - Sending requests that return binary data (images, audio)
 * - Support for dynamic headers and request bodies
 */
public class RequestHandler {

    /**
     * Sends an HTTP request and returns the response as a String.
     *
     * Supports POST and GET methods. Logs status code and response to stdout.
     * Returns null if the request fails.
     *
     * @param url the endpoint URL to request
     * @param method HTTP method ("POST" or "GET")
     * @param jsonBody request body (JSON string, can be null for GET requests)
     * @param headers map of HTTP headers to include in the request
     * @return response body as a String, or null if any error occurs
     * @throws IllegalArgumentException if the HTTP method is not "POST" or "GET"
     */
    public static String sendHttpRequest( String url, String method, String jsonBody, Map<String, String> headers) {
        String response = null;

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

            // Add headers dynamically
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }

            // Handle method
            if ("POST".equalsIgnoreCase(method)) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : ""));
            } else if ("GET".equalsIgnoreCase(method)) {
                requestBuilder.GET();
            } else {
                throw new IllegalArgumentException("Unsupported method: " + method);
            }

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            response = httpResponse.body();

            System.out.println("Status Code: " + httpResponse.statusCode());
            System.out.println("Response: " + response);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Sends an HTTP request and returns the response as raw bytes.
     *
     * Supports POST and GET methods. Logs status code to stdout.
     * Returns null if the request fails.
     *
     * Useful for binary data responses (images, audio files).
     *
     * @param url the endpoint URL to request
     * @param method HTTP method ("POST" or "GET")
     * @param jsonBody request body (JSON string, can be null for GET requests)
     * @param headers map of HTTP headers to include in the request
     * @return response body as a byte array, or null if any error occurs
     * @throws IllegalArgumentException if the HTTP method is not "POST" or "GET"
     */
    public static byte[] sendHttpRequestBytes(String url, String method, String jsonBody, Map<String, String> headers) {
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

            HttpRequest request = requestBuilder.build();

            HttpResponse<byte[]> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            System.out.println("Status Code: " + httpResponse.statusCode());

            return httpResponse.body();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
