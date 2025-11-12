package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Tour entities.
 */
@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
}
