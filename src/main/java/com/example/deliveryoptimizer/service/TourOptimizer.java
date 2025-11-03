package com.example.deliveryoptimizer.service;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import java.util.List;

/**
 * Interface for tour optimization algorithms.
 * Implementations should compute an ordered list of deliveries representing an optimal tour
 * starting/ending or operating from the provided warehouse as appropriate.
 */
public interface TourOptimizer {

    /**
     * Calculate an (optimal or heuristic) route for the given deliveries and warehouse.
     *
     * @param deliveries list of deliveries to include in the tour (may be unsorted)
     * @param warehouse  the warehouse / depot used as origin (and possibly destination)
     * @return ordered list of deliveries representing the calculated tour
     */
    List<Delivery> calculateOptimalTour(List<Delivery> deliveries, Warehouse warehouse);
}
