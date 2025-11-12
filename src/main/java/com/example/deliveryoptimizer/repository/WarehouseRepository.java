package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Warehouse entities.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
