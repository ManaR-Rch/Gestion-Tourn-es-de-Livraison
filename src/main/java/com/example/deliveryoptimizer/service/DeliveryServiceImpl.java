package com.example.deliveryoptimizer.service;

import com.example.deliveryoptimizer.entity.Delivery;
import com.example.deliveryoptimizer.repository.DeliveryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of {@link DeliveryService} that delegates to
 * {@link DeliveryRepository}. No Spring annotations are used so this
 * implementation can be declared and wired from XML (`applicationContext.xml`).
 */
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
}
