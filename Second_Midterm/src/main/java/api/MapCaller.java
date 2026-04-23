package api;

import handlers.RequestHandler;
import io.github.cdimascio.dotenv.Dotenv;
import utils.GeoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for making HTTP requests to the Geoapify static map API.
 * Geographic calculations are delegated to GeoUtils.
 */
public class MapCaller extends Connectable {
    private String apiKey;

    public MapCaller() {
        Dotenv dotenv = Dotenv.load();
        setApiKey(dotenv.get("MAP_API_KEY"));
    }

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

    public void setApiKey(String token) { this.apiKey = token; }
    public String getApiKey()           { return this.apiKey; }
}
