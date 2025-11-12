package com.example.deliveryoptimizer.service.impl;

import org.springframework.stereotype.Service;

import com.example.deliveryoptimizer.entity.Vehicle;
import com.example.deliveryoptimizer.repository.VehicleRepository;
import com.example.deliveryoptimizer.service.VehicleService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vehicle service implementation. Wired via XML (no annotations).
 */
@Service
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;

  public VehicleServiceImpl(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }

  @Override
  public List<Vehicle> getVehiclesByType(String type) {

    // Use repository method that returns results (optionally already ordered)
    List<Vehicle> vehicles = vehicleRepository.findByType(type);

    // Sort in-memory by capacityWeight descending using Stream API
    return vehicles.stream()
        .sorted(Comparator.comparingDouble(Vehicle::getCapacityWeight).reversed())
        .collect(Collectors.toList());
  }
}
