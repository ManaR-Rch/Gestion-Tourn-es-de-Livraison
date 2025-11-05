package com.example.deliveryoptimizer.service;

import com.example.deliveryoptimizer.entity.Vehicle;
import java.util.List;

/**
 * Service contract for vehicle-related operations.
 */
public interface VehicleService {

  /**
   * Return vehicles of the given type, sorted by capacityWeight descending.
   * 
   * @param type vehicle type string, e.g. "VAN"
   * @return list of vehicles sorted by capacityWeight descending
   */
  List<Vehicle> getVehiclesByType(String type);
}
