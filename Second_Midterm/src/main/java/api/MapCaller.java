package api;

import handlers.RequestHandler;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

public class MapCaller extends Connectable{
    private String apiKey;

    public MapCaller(){
        Dotenv dotenv = Dotenv.load();
        setApiKey(dotenv.get("MAP_API_KEY"));
    }

    public byte[] mapRequest(double[] startCoords, double[] endCoords){
        double[] centerCoords = calculateMapCenter(startCoords, endCoords);
        int zoomLevel = calculateZoom(startCoords, endCoords);

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
                """.formatted(centerCoords[0], centerCoords[1], zoomLevel, startCoords[0], startCoords[1], endCoords[0], endCoords[1]);

        return RequestHandler.sendHttpRequestBytes(url, "POST", jsonBody, headers);
    }

    private static double[] calculateMapCenter(double[] startCoords, double[] endCoords) {
        double centerLat = (endCoords[0] + startCoords[0]) / 2;
        double centerLon = (endCoords[1] + startCoords[1]) / 2;

        System.out.println("Center: Latitude" + centerLat + "Longitude" + centerLon);

        return new double[]{centerLat, centerLon};
    }

    private static int calculateZoom(double[] startCoords, double[] endCoords) {
        double latDiff = Math.abs(endCoords[0] - startCoords[0]);
        double lonDiff = Math.abs(endCoords[1] - startCoords[1]);
        double maxDiff = Math.max(latDiff, lonDiff);

        if (maxDiff > 50)  return 1;
        if (maxDiff > 20)  return 2;
        if (maxDiff > 10)  return 4;
        if (maxDiff > 5)   return 6;
        if (maxDiff > 2)   return 7;
        if (maxDiff > 1)   return 8;
        if (maxDiff > 0.5) return 10;
        if (maxDiff > 0.1) return 12;
        return 14;
    }

    public void setApiKey(String token){
        this.apiKey = token;
    }

    public String getApiKey(){
        return this.apiKey;
    }
}
