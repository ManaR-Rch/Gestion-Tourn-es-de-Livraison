package com.example.deliveryoptimizer.service.impl;

import org.springframework.stereotype.Service;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.repository.DeliveryRepository;
import com.example.deliveryoptimizer.service.DeliveryService;
import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.mapper.DeliveryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of {@link DeliveryService} that delegates to
 * {@link DeliveryRepository}. No Spring annotations are used so this
 * implementation can be declared and wired from XML (`applicationContext.xml`).
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {

  private final DeliveryRepository deliveryRepository;

  // Constructor-style injection (suitable for XML wiring)
  public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
    this.deliveryRepository = deliveryRepository;
  }

  @Override
  public List<Delivery> findAll() {
    return deliveryRepository.findAll();
  }

  @Override
  public Optional<Delivery> findById(Long id) {
    return deliveryRepository.findById(id);
  }

  @Override
  public Delivery save(Delivery delivery) {
    return deliveryRepository.save(delivery);
  }

  @Override
  public void deleteById(Long id) {
    deliveryRepository.deleteById(id);
  }

  @Override
  public Page<DeliveryDto> getDeliveriesByStatus(String status, int page, int size) {
    Page<Delivery> deliveries = deliveryRepository.findByStatus(status, PageRequest.of(page, size));
    // Convert Page<Delivery> to Page<DeliveryDto> using map()
    return deliveries.map(DeliveryMapper::toDto);
  }

  @Override
  public Page<DeliveryDto> getAllDeliveries(int page, int size) {
    Page<Delivery> deliveries = deliveryRepository.findAll(PageRequest.of(page, size));
    return deliveries.map(DeliveryMapper::toDto);
  }
}
