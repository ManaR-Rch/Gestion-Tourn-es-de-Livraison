package com.example.deliveryoptimizer.service.impl;

import org.springframework.stereotype.Service;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.service.TourOptimizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple deterministic nearest-neighbor tour optimizer.
 *
 * Algorithm:
 * - Start at the warehouse coordinates
 * - While there remain unvisited deliveries, pick the closest (Haversine
 * distance)
 * to the current point and mark it visited; append it to the result list.
 * - Return the ordered list of deliveries. (Returning to warehouse is implicit)
 *
 * Complexity: O(n^2) in number of deliveries.
 */
@Service
public class NearestNeighborOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Warehouse warehouse) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        // copy to avoid mutating caller list
        List<Delivery> remaining = new ArrayList<>(deliveries);
        List<Delivery> route = new ArrayList<>(deliveries.size());

        // start point: warehouse coordinates
        double curLat = warehouse != null ? warehouse.getLatitude() : 0.0;
        double curLon = warehouse != null ? warehouse.getLongitude() : 0.0;

        while (!remaining.isEmpty()) {
            int bestIndex = -1;
            double bestDist = Double.POSITIVE_INFINITY;

            for (int i = 0; i < remaining.size(); i++) {
                Delivery d = remaining.get(i);
                double dist = distance(curLat, curLon, d.getLatitude(), d.getLongitude());
                // deterministic tie-breaker: if equal distance, choose smaller id (if
                // available)
                if (dist < bestDist || (dist == bestDist && tieBreak(d, remaining.get(bestIndex)))) {
                    bestDist = dist;
                    bestIndex = i;
                }
            }

            // pick the nearest
            Delivery pick = remaining.remove(bestIndex);
            route.add(pick);

            // move current point to this delivery
            curLat = pick.getLatitude();
            curLon = pick.getLongitude();
        }

        return route;
    }

    /**
     * Deterministic tie-breaker: prefer delivery with smaller id when both
     * non-null.
     */
    private boolean tieBreak(Delivery candidate, Delivery currentBest) {
        if (currentBest == null)
            return true;
        Long cid = candidate.getId();
        Long bid = currentBest.getId();
        if (cid == null && bid == null)
            return false; // keep existing
        if (cid == null)
            return false;
        if (bid == null)
            return true;
        return cid.compareTo(bid) < 0;
    }

    /**
     * Haversine distance between two points in meters.
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6_371_000; // Earth radius in meters
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2 - lat1);
        double Δλ = Math.toRadians(lon2 - lon1);

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
                + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
