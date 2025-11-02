package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Vehicle entities.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
