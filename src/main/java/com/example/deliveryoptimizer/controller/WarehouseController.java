package com.example.deliveryoptimizer.controller;

import com.example.deliveryoptimizer.entity.Warehouse;
import com.example.deliveryoptimizer.repository.WarehouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Annotated Warehouse controller.
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> listAll() {
        return ResponseEntity.ok(warehouseRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse w) {
        Warehouse saved = warehouseRepository.save(w);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
