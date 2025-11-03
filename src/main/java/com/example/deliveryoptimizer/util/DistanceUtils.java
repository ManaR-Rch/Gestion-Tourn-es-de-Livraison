package com.example.deliveryoptimizer.util;

/**
 * Utility class for geographic distance calculations.
 */
public class DistanceUtils {

    /**
     * Haversine formula: returns the great-circle distance between two points on the Earth in meters.
     *
     * @param lat1 latitude of point 1 in degrees
     * @param lon1 longitude of point 1 in degrees
     * @param lat2 latitude of point 2 in degrees
     * @param lon2 longitude of point 2 in degrees
     * @return distance in meters
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6_371_000; // Earth radius in meters
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Small demo to run the haversine method from the command line / IDE.
     */
    public static void main(String[] args) {
        // Example: Paris (48.8566, 2.3522) -> London (51.5074, -0.1278)
        double latParis = 48.8566;
        double lonParis = 2.3522;
        double latLondon = 51.5074;
        double lonLondon = -0.1278;

        double meters = haversine(latParis, lonParis, latLondon, lonLondon);
        System.out.printf("Distance Paris-London = %.0f m (%.2f km)%n", meters, meters / 1000.0);
    }
}
