package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for Delivery entities.
 * Spring Data JPA will provide an implementation at runtime when repositories
 * are enabled.
 */
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Example derived query method: find deliveries by status
    List<Delivery> findByStatus(String status);

    // 2) Derived query with pagination
    Page<Delivery> findByStatus(String status, Pageable pageable);

    // 3) Example JPQL query (search deliveries where status contains the provided
    // value)
    @Query("SELECT d FROM Delivery d WHERE d.status LIKE CONCAT('%', :status, '%')")
    Page<Delivery> searchByStatusLike(@Param("status") String status, Pageable pageable);

}
