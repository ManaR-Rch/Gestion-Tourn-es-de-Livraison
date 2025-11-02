package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Tour entities.
 */
public interface TourRepository extends JpaRepository<Tour, Long> {
}
