package utils;

/**
 * Utilities for geographic coordinate calculations.
 *
 * Computes optimal map visualization parameters for geographic routes:
 * - Center point between start and end
 * - Appropriate zoom level based on distance
 *
 * Kept separate from API concerns to maintain clean separation of concerns.
 */
public class GeoUtils {

    /**
     * Calculates the optimal map center point between two coordinates.
     *
     * Center is the midpoint between start and end coordinates.
     *
     * @param startCoords [latitude, longitude] of route start
     * @param endCoords [latitude, longitude] of route end
     * @return [centerLatitude, centerLongitude]
     */
    public static double[] calculateMapCenter(double[] startCoords, double[] endCoords) {
        double centerLat = (endCoords[0] + startCoords[0]) / 2;
        double centerLon = (endCoords[1] + startCoords[1]) / 2;

        System.out.println("Center: Latitude" + centerLat + "Longitude" + centerLon);

        return new double[]{centerLat, centerLon};
    }

    /**
     * Calculates an appropriate zoom level for viewing a geographic route.
     *
     * Zoom level (0-20) is determined by the distance between points:
     * - Large distances (>50°): zoom 1 (world view)
     * - Regional distances (10-50°): zoom 2-4
     * - City/area distances (0.5-10°): zoom 6-8
     * - Street level (<0.5°): zoom 10-14
     *
     * @param startCoords [latitude, longitude] of route start
     * @param endCoords [latitude, longitude] of route end
     * @return zoom level appropriate for viewing the route
     */
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
