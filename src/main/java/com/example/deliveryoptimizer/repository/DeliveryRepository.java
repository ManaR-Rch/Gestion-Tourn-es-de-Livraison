package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Delivery entities.
 * Spring Data JPA will provide an implementation at runtime when repositories are enabled.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Example derived query method: find deliveries by status
    List<Delivery> findByStatus(String status);
}
