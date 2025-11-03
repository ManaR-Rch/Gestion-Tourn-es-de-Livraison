package com.example.deliveryoptimizer.service.impl;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.service.TourOptimizer;
import com.example.deliveryoptimizer.service.TourService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple TourService implementation that delegates to one of the provided TourOptimizer implementations.
 * Note: beans (optimizers and this service) should be configured in XML (`applicationContext.xml`).
 */
public class TourServiceImpl implements TourService {

    private final TourOptimizer defaultOptimizer;
    private final Map<String, TourOptimizer> optimizers = new HashMap<>();

    /**
     * Constructor: pass the default optimizer first (e.g. ClarkeWright), then any other optimizer(s).
     * We store them under keys "CLARKE" and "NEAREST" for selection. Beans must be wired in XML.
     */
    public TourServiceImpl(TourOptimizer defaultOptimizer, TourOptimizer otherOptimizer) {
        this.defaultOptimizer = defaultOptimizer;
        if (defaultOptimizer != null) this.optimizers.put("CLARKE", defaultOptimizer);
        if (otherOptimizer != null) this.optimizers.put("NEAREST", otherOptimizer);
    }

    @Override
    public List<Delivery> getOptimizedTour(List<Delivery> deliveries, String optimizerName, Long vehicleId) {
        TourOptimizer opt = defaultOptimizer;
        if (optimizerName != null) {
            TourOptimizer chosen = optimizers.get(optimizerName.toUpperCase());
            if (chosen != null) opt = chosen;
        }
        // vehicleId can be used to lookup vehicle capacity / constraints in future
        return opt.calculateOptimalTour(deliveries, null);
    }

    @Override
    public double getTotalDistance(List<Delivery> deliveries, Warehouse warehouse) {
        if (deliveries == null || deliveries.isEmpty()) return 0.0;
        double total = 0.0;
        double curLat = warehouse != null ? warehouse.getLatitude() : deliveries.get(0).getLatitude();
        double curLon = warehouse != null ? warehouse.getLongitude() : deliveries.get(0).getLongitude();

        for (Delivery d : deliveries) {
            total += NearestNeighborOptimizer.distance(curLat, curLon, d.getLatitude(), d.getLongitude());
            curLat = d.getLatitude();
            curLon = d.getLongitude();
        }
        // return to warehouse
        if (warehouse != null) {
            total += NearestNeighborOptimizer.distance(curLat, curLon, warehouse.getLatitude(), warehouse.getLongitude());
        }
        return total;
    }
}
