package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Warehouse entities.
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
