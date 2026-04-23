package utils;

/**
 * Responsible for geographic coordinate calculations.
 * Kept separate from API-calling concerns in MapCaller.
 */
public class GeoUtils {

    public static double[] calculateMapCenter(double[] startCoords, double[] endCoords) {
        double centerLat = (endCoords[0] + startCoords[0]) / 2;
        double centerLon = (endCoords[1] + startCoords[1]) / 2;

        System.out.println("Center: Latitude" + centerLat + "Longitude" + centerLon);

        return new double[]{centerLat, centerLon};
    }

    public static int calculateZoom(double[] startCoords, double[] endCoords) {
        double latDiff  = Math.abs(endCoords[0] - startCoords[0]);
        double lonDiff  = Math.abs(endCoords[1] - startCoords[1]);
        double maxDiff  = Math.max(latDiff, lonDiff);

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
}
