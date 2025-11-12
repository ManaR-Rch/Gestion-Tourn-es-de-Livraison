package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.entity.Vehicle;
import com.example.deliveryoptimizer.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Annotated controller exposing GET /api/vehicles/by-type?type=...
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

  private final VehicleService vehicleService;

  public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  // GET /api/vehicles/by-type?type=VAN
  @GetMapping("/by-type")
  public ResponseEntity<List<Vehicle>> byType(@RequestParam("type") String type) {
    if (type == null || type.trim().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    List<Vehicle> result = vehicleService.getVehiclesByType(type.trim());
    return ResponseEntity.ok(result);
  }
}
