package com.example.deliveryoptimizer.service;

import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.entity.Delivery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for Delivery-related operations.
 *
 * This is a simple interface so the implementation can be wired in XML
 * (no Spring annotations are used here so existing XML-first wiring
 * remains intact).
 */
public interface DeliveryService {

  List<Delivery> findAll();

  Optional<Delivery> findById(Long id);

  Delivery save(Delivery delivery);

  void deleteById(Long id);

  Page<DeliveryDto> getDeliveriesByStatus(String status, int page, int size);

  Page<DeliveryDto> getAllDeliveries(int page, int size);

}
