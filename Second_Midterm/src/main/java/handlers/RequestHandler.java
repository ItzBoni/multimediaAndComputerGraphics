package handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Map;

public class RequestHandler {

    /**
     *
     * @param url URL for the API connection.
     * @param method "POST" || "GET".
     * @param jsonBody JSON Body of the request, can be null for GET requests.
     * @param headers Map of headers to include in the response. Can be any amount.
     * @return response body as a String, or null if any error occurs.
     * @throws IllegalArgumentException if the HTTP method is not supported.
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
