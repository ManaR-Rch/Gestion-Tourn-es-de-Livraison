package com.example.deliveryoptimizer.repository;

import com.example.deliveryoptimizer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Repository interface for Vehicle entities.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	// 1) Derived query - Spring builds the SQL from the method name
	List<Vehicle> findByType(String type);

	// 2) Derived query with ordering - DB will return results sorted by capacityWeight desc
	List<Vehicle> findByTypeOrderByCapacityWeightDesc(String type);

	// Optional explicit JPQL query example (commented out):
	// @Query("SELECT v FROM Vehicle v WHERE v.type = :type")
	// List<Vehicle> findByTypeQuery(@Param("type") String type);
}
