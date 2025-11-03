package com.example.deliveryoptimizer.service.impl;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.service.TourOptimizer;

import java.util.*;

/**
 * Simple Clarke-Wright Savings optimizer (basic version).
 * - Computes savings for every pair: s(i,j) = dist(W,i)+dist(W,j)-dist(i,j)
 * - Sorts pairs by savings descending
 * - Attempts to merge routes if pair endpoints match and capacity (total
 * weight) allows
 * - Uses a simple fixed vehicle capacity (constant) for checks
 *
 * Note: This is a straightforward educational implementation (not
 * production-grade).
 */
public class ClarkeWrightOptimizer implements TourOptimizer {

    // Simple capacity used for demonstration (kg). Replace by vehicle-specific
    // capacity later.
    private static final double DEFAULT_VEHICLE_CAPACITY = 1000.0;

    @Override
    public List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Warehouse warehouse) {
        if (deliveries == null || deliveries.isEmpty())
            return new ArrayList<>();

        final double depotLat = warehouse != null ? warehouse.getLatitude() : 0.0;
        final double depotLon = warehouse != null ? warehouse.getLongitude() : 0.0;

        int n = deliveries.size();

        // Initial routes: each delivery is its own route (sequence of deliveries)
        List<List<Delivery>> routes = new ArrayList<>();
        List<Double> routeWeights = new ArrayList<>();
        for (Delivery d : deliveries) {
            List<Delivery> r = new ArrayList<>();
            r.add(d);
            routes.add(r);
            routeWeights.add(d.getWeight());
        }

        // Precompute distances between depot and deliveries and between deliveries
        double[] distDepot = new double[n];
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            Delivery di = deliveries.get(i);
            distDepot[i] = NearestNeighborOptimizer.distance(depotLat, depotLon, di.getLatitude(), di.getLongitude());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    dist[i][j] = 0.0;
                else {
                    Delivery di = deliveries.get(i);
                    Delivery dj = deliveries.get(j);
                    dist[i][j] = NearestNeighborOptimizer.distance(di.getLatitude(), di.getLongitude(),
                            dj.getLatitude(), dj.getLongitude());
                }
            }
        }

        // Compute savings list
        class Saving {
            int i;
            int j;
            double s;
        }
        List<Saving> savings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double s = distDepot[i] + distDepot[j] - dist[i][j];
                Saving sv = new Saving();
                sv.i = i;
                sv.j = j;
                sv.s = s;
                savings.add(sv);
            }
        }

        // Sort savings descending
        savings.sort((a, b) -> Double.compare(b.s, a.s));

        // Helper: find route index and whether delivery is at start or end
        // (Use the class-level nested RoutePos defined below)

        // Try to merge routes according to savings
        for (Saving sv : savings) {
            int idxI = sv.i;
            int idxJ = sv.j;

            RoutePos posI = findRouteContaining(routes, deliveries.get(idxI));
            RoutePos posJ = findRouteContaining(routes, deliveries.get(idxJ));

            if (posI == null || posJ == null)
                continue; // safety
            if (posI.routeIdx == posJ.routeIdx)
                continue; // same route

            int rI = posI.routeIdx;
            int rJ = posJ.routeIdx;

            // Determine if endpoints align for a merge (end of one to start of the other)
            boolean canMerge = false;
            boolean iAtEnd = posI.atEnd;
            boolean iAtStart = posI.atStart;
            boolean jAtEnd = posJ.atEnd;
            boolean jAtStart = posJ.atStart;

            // We'll support two cases: rI end -> rJ start OR rJ end -> rI start
            if (iAtEnd && jAtStart) {
                canMerge = true;
            } else if (jAtEnd && iAtStart) {
                // swap roles to keep merge direction consistent: merge rJ -> rI
                // implement by swapping rI and rJ
                int tmp = rI;
                rI = rJ;
                rJ = tmp;
                canMerge = true;
            }

            if (!canMerge)
                continue;

            // Check capacity (simple sum of weights)
            double combinedWeight = routeWeights.get(rI) + routeWeights.get(rJ);
            if (combinedWeight > DEFAULT_VEHICLE_CAPACITY) {
                // cannot merge due to capacity
                continue;
            }

            // perform merge: append route rJ to route rI
            List<Delivery> merged = new ArrayList<>(routes.get(rI));
            merged.addAll(routes.get(rJ));

            // replace rI with merged, remove rJ
            routes.set(rI, merged);
            routeWeights.set(rI, combinedWeight);
            // remove higher index first to avoid shifting issues
            int removeIdx = rJ;
            // If rJ < rI then rI index shifts after removal
            if (rJ < rI) {
                routes.remove(removeIdx);
                routeWeights.remove(removeIdx);
                rI = rI - 1;
            } else {
                routes.remove(removeIdx);
                routeWeights.remove(removeIdx);
            }
        }

        // Flatten routes into a single ordered list (concatenate routes)
        List<Delivery> result = new ArrayList<>();
        for (List<Delivery> r : routes)
            result.addAll(r);
        return result;
    }

    /**
     * Find the route index containing a delivery and whether it's at start/end.
     */
    private RoutePos findRouteContaining(List<List<Delivery>> routes, Delivery d) {
        for (int ri = 0; ri < routes.size(); ri++) {
            List<Delivery> r = routes.get(ri);
            if (r.isEmpty())
                continue;
            if (r.get(0).equals(d)) {
                RoutePos p = new RoutePos();
                p.routeIdx = ri;
                p.atStart = true;
                p.atEnd = (r.size() == 1);
                return p;
            }
            if (r.get(r.size() - 1).equals(d)) {
                RoutePos p = new RoutePos();
                p.routeIdx = ri;
                p.atStart = (r.size() == 1);
                p.atEnd = true;
                return p;
            }
            // if in middle, we don't allow merge via that delivery
            for (int k = 1; k < r.size() - 1; k++) {
                if (r.get(k).equals(d)) {
                    RoutePos p = new RoutePos();
                    p.routeIdx = ri;
                    p.atStart = false;
                    p.atEnd = false;
                    return p;
                }
            }
        }
        return null;
    }

    // Local helper classes used inside methods
    private static class RoutePos {
        int routeIdx;
        boolean atStart;
        boolean atEnd;
    }

}
