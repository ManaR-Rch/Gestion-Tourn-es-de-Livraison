package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.mapper.DeliveryMapper;
import com.example.deliveryoptimizer.repository.DeliveryRepository;
import com.example.deliveryoptimizer.service.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Simple DeliveryController (XML-configured) exposing REST-like endpoints:
 * - GET /api/deliveries -> list all deliveries
 * - POST /api/deliveries -> create a delivery (body: Delivery JSON)
 * - PUT /api/deliveries/{id} -> update an existing delivery
 * - DELETE /api/deliveries/{id} -> delete by id
 *
 * No @Autowired: repositories are injected via constructor and beans are
 * declared
 * in applicationContext.xml.
 */
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryRepository deliveryRepository, DeliveryService deliveryService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryService = deliveryService;
    }

    // GET /api/deliveries
    // Optional params: status, page, size
    @GetMapping
    public ResponseEntity<?> listAll(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        if (status != null && !status.isBlank()) {
            Page<DeliveryDto> pageResult = deliveryService.getDeliveriesByStatus(status, page, size);
            return ResponseEntity.ok(pageResult);
        }
        // no status: return paginated list of all deliveries as DTOs
        Page<DeliveryDto> pageResult = deliveryService.getAllDeliveries(page, size);
        return ResponseEntity.ok(pageResult);
    }

    // POST /api/deliveries
    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody Delivery delivery) {
        Delivery saved = deliveryRepository.save(delivery);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/deliveries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DeliveryDto updDto) {
        Optional<Delivery> existing = deliveryRepository.findById(id);
        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Delivery toUpdate = existing.get();
        // Map fields from DTO to entity; keep id from path
        Delivery incoming = DeliveryMapper.toEntity(updDto);
        // apply allowed updates
        toUpdate.setLatitude(incoming.getLatitude());
        toUpdate.setLongitude(incoming.getLongitude());
        toUpdate.setWeight(incoming.getWeight());
        toUpdate.setVolume(incoming.getVolume());
        toUpdate.setStatus(incoming.getStatus());

        Delivery saved = deliveryService.save(toUpdate);
        DeliveryDto resp = DeliveryMapper.toDto(saved);
        return ResponseEntity.ok(resp);
    }

    // DELETE /api/deliveries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
