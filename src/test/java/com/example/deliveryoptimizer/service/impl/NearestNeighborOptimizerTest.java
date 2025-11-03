package com.example.deliveryoptimizer.service.impl;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NearestNeighborOptimizerTest {

    @Test
    void whenFourDeliveriesAroundWarehouse_thenResultContainsSameElements_andFirstIsClosest() {
        // warehouse at (0,0)
        Warehouse wh = new Warehouse("TestWh", 0.0, 0.0, "08:00-18:00");

        // create 4 deliveries at increasing distances from the warehouse (lat differs, lon = 0)
        Delivery d1 = new Delivery(0.0001, 0.0, 1.0, 0.1, "PENDING"); // closest
        Delivery d2 = new Delivery(0.001, 0.0, 1.0, 0.1, "PENDING");
        Delivery d3 = new Delivery(0.002, 0.0, 1.0, 0.1, "PENDING");
        Delivery d4 = new Delivery(0.01, 0.0, 1.0, 0.1, "PENDING");

        List<Delivery> input = Arrays.asList(d1, d2, d3, d4);

        NearestNeighborOptimizer optimizer = new NearestNeighborOptimizer();
        List<Delivery> result = optimizer.calculateOptimalTour(input, wh);

        // same number of elements
        assertEquals(input.size(), result.size(), "Optimized tour should have same number of deliveries");

        // contains the same elements (by reference)
        assertTrue(result.containsAll(input), "Result should contain all original deliveries");

        // first element should be the closest (d1)
        assertSame(d1, result.get(0), "The first delivery should be the closest to the warehouse");
    }
}
