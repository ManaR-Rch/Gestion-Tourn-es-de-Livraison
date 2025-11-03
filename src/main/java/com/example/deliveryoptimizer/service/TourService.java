package com.example.deliveryoptimizer.service;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;

import java.util.List;

/**
 * Service interface that exposes tour-related operations.
 */
public interface TourService {

    /**
     * Calculate an optimized tour using the named optimizer.
     *
     * @param deliveries   list of deliveries to include
     * @param optimizerName optimizer identifier (e.g. "NEAREST" or "CLARKE"); if null uses default
     * @param vehicleId    id of the vehicle to be used (can be used to get capacity/constraints)
     * @return ordered list of deliveries representing the tour
     */
    List<Delivery> getOptimizedTour(List<Delivery> deliveries, String optimizerName, Long vehicleId);

    /**
     * Compute the total distance (meters) of a tour starting and ending at the warehouse.
     */
    double getTotalDistance(List<Delivery> deliveries, Warehouse warehouse);

}
