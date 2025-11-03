package com.example.deliveryoptimizer.mapper;

import com.example.deliveryoptimizer.dto.DeliveryDto;
import com.example.deliveryoptimizer.entity.Delivery;

/**
 * Manual mapper between Delivery entity and DeliveryDto.
 * Simple, explicit conversion methods.
 */
public class DeliveryMapper {

    /**
     * Convert entity to DTO. Returns null if input is null.
     */
    public static DeliveryDto toDto(Delivery e) {
        if (e == null)
            return null;
        return new DeliveryDto(
                e.getId(),
                e.getLatitude(),
                e.getLongitude(),
                e.getWeight(),
                e.getVolume(),
                e.getStatus());
    }

    /**
     * Convert DTO to entity. If dto.getId() is null, entity id remains null (new
     * entity).
     */
    public static Delivery toEntity(DeliveryDto d) {
        if (d == null)
            return null;
        Delivery e = new Delivery();
        e.setId(d.getId());
        e.setLatitude(d.getLatitude());
        e.setLongitude(d.getLongitude());
        e.setWeight(d.getWeight());
        e.setVolume(d.getVolume());
        e.setStatus(d.getStatus());
        return e;
    }
}
