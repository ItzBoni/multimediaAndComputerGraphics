package api;

import handlers.RequestHandler;
import utils.GeoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for Geoapify static map API.
 *
 * Generates map images showing the geographic journey between two points.
 * Handles:
 * - Computing optimal map center point
 * - Calculating appropriate zoom level
 * - Creating map request with start/end markers
 *
 * Geographic calculations are delegated to GeoUtils.
 * Loads API key from system environment variable at initialization.
 */
public class MapCaller extends Connectable {
    private String apiKey;

    /**
     * Initializes MapCaller and loads Geoapify API key from system environment.
     * Reads GeoapifyToken environment variable.
     *
     * @throws RuntimeException if GeoapifyToken environment variable is not set
     */
    public MapCaller() {
        String token = System.getenv("GeoapifyToken");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("GeoapifyToken environment variable is not set");
        }
        setApiKey(token);
    }

    /**
     * Generates a map image showing the route between two geographic points.
     *
     * Creates a static map with:
     * - Red marker at start coordinates
     * - Blue marker at end coordinates
     * - Optimal zoom level and center point for the route
     *
     * @param startCoords [latitude, longitude] of journey start
     * @param endCoords [latitude, longitude] of journey end
     * @return raw PNG image bytes of the generated map
     */
    public byte[] mapRequest(double[] startCoords, double[] endCoords) {
        double[] centerCoords = GeoUtils.calculateMapCenter(startCoords, endCoords);
        int zoomLevel         = GeoUtils.calculateZoom(startCoords, endCoords);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String url = "https://maps.geoapify.com/v1/staticmap?apiKey=" + getApiKey();
        String jsonBody = """
                {
                    "style": "osm-bright",
                    "scaleFactor": 2,
                    "width": 720,
                    "height": 1200,
                    "center": {
                        "lat": %s,
                        "lon": %s
                    },
                    "zoom": %s,
                    "markers": [
                        {
                            "lat": %s,
                            "lon": %s,
                            "type": "material",
                            "color": "#ff3421",
                            "icontype": "awesome"
                        },
                        {
                            "lat": %s,
                            "lon": %s,
                            "type": "material",
                            "color": "#3421ff",
                            "icontype": "awesome"
                        }
                    ]
                }
                """.formatted(centerCoords[0], centerCoords[1], zoomLevel,
                              startCoords[0], startCoords[1],
                              endCoords[0], endCoords[1]);

        return RequestHandler.sendHttpRequestBytes(url, "POST", jsonBody, headers);
    }

    /**
     * Sets the Geoapify API key for authentication.
     *
     * @param token the API key to use
     */
    public void setApiKey(String token) { this.apiKey = token; }

    /**
     * Gets the current Geoapify API key.
     *
     * @return the API key
     */
    public String getApiKey() { return this.apiKey; }
}
