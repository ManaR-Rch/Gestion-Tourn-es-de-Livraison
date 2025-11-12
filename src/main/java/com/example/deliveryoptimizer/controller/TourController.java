package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.mapper.DeliveryMapper;
import com.example.deliveryoptimizer.repository.DeliveryRepository;
import com.example.deliveryoptimizer.repository.WarehouseRepository;
import com.example.deliveryoptimizer.service.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Annotated REST controller for Tour optimization.
 * Replaces the legacy Controller implementation and exposes POST
 * /api/tours/optimize
 *
 * Note: services/repositories are left unchanged and can still be configured in
 * XML.
 */
@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final DeliveryRepository deliveryRepository;
    private final WarehouseRepository warehouseRepository;
    private final TourService tourService;

    public TourController(DeliveryRepository deliveryRepository,
            WarehouseRepository warehouseRepository,
            TourService tourService) {
        this.deliveryRepository = deliveryRepository;
        this.warehouseRepository = warehouseRepository;
        this.tourService = tourService;
    }

    public static class OptimizeRequest {
        public Long warehouseId;
        public List<Long> deliveryIds = new ArrayList<>();
        public String optimizer;
    }

    @PostMapping("/optimize")
    public ResponseEntity<?> optimize(@RequestBody OptimizeRequest req) {
        try {
            // load warehouse if provided
            Warehouse warehouse = null;
            if (req.warehouseId != null) {
                Optional<Warehouse> wOpt = warehouseRepository.findById(req.warehouseId);
                if (wOpt.isPresent()) {
                    warehouse = wOpt.get();
                }
            }

            // load deliveries
            List<Delivery> deliveries = new ArrayList<>();
            if (req.deliveryIds != null && !req.deliveryIds.isEmpty()) {
                deliveryRepository.findAllById(req.deliveryIds).forEach(deliveries::add);
            }

            // call existing service (keeps business logic unchanged)
            List<Delivery> optimized = tourService.getOptimizedTour(deliveries, req.optimizer, null);

            // map to DTOs using existing DeliveryMapper
            List<DeliveryDto> dtos = optimized.stream()
                    .map(DeliveryMapper::toDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "invalid JSON or missing fields"));
        }
    }

}
