package com.example.deliveryoptimizer.service.impl;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Tour;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.repository.TourRepository;
import com.example.deliveryoptimizer.service.TourOptimizer;
import com.example.deliveryoptimizer.service.TourService;

import java.time.LocalDate;
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
    private final TourRepository tourRepository;

    /**
     * Constructor: pass the default optimizer first (e.g. ClarkeWright), then any other optimizer(s),
     * and finally the TourRepository for persisting generated tours. Beans must be wired in XML.
     */
    public TourServiceImpl(TourOptimizer defaultOptimizer, TourOptimizer otherOptimizer, TourRepository tourRepository) {
        this.defaultOptimizer = defaultOptimizer;
        if (defaultOptimizer != null) this.optimizers.put("CLARKE", defaultOptimizer);
        if (otherOptimizer != null) this.optimizers.put("NEAREST", otherOptimizer);
        this.tourRepository = tourRepository;
    }

    @Override
    public List<Delivery> getOptimizedTour(List<Delivery> deliveries, String optimizerName, Long vehicleId) {
        TourOptimizer opt = defaultOptimizer;
        if (optimizerName != null) {
            TourOptimizer chosen = optimizers.get(optimizerName.toUpperCase());
            if (chosen != null) opt = chosen;
        }

        // vehicleId can be used to lookup vehicle capacity / constraints in future
        List<Delivery> ordered = opt.calculateOptimalTour(deliveries, null);

        // Persist the tour and associate deliveries to it
        try {
            Tour tour = new Tour();
            tour.setDate(LocalDate.now());
            // add deliveries and update back-reference
            if (ordered != null) {
                for (Delivery d : ordered) {
                    tour.addDelivery(d);
                }
            }
            // save tour (will cascade to deliveries because of CascadeType.ALL on Tour.deliveries)
            tourRepository.save(tour);
        } catch (Exception ex) {
            // swallow persistence exceptions to keep previous behaviour (still return ordered list)
            // In future we can log this exception or propagate depending on requirements.
        }

        return ordered;
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
