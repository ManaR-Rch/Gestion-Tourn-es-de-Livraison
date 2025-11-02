package com.example.deliveryoptimizer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Vehicle entity: represents a vehicle used for tours/deliveries.
 * Fields:
 * - id: primary key
 * - type: e.g. "truck", "van"
 * - capacityWeight: max weight capacity (kg)
 * - capacityVolume: max volume capacity (m3)
 * - maxDeliveries: maximum number of deliveries this vehicle can do in one tour
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private double capacityWeight;

    private double capacityVolume;

    private int maxDeliveries;

    public Vehicle() {
    }

    public Vehicle(String type, double capacityWeight, double capacityVolume, int maxDeliveries) {
        this.type = type;
        this.capacityWeight = capacityWeight;
        this.capacityVolume = capacityVolume;
        this.maxDeliveries = maxDeliveries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCapacityWeight() {
        return capacityWeight;
    }

    public void setCapacityWeight(double capacityWeight) {
        this.capacityWeight = capacityWeight;
    }

    public double getCapacityVolume() {
        return capacityVolume;
    }

    public void setCapacityVolume(double capacityVolume) {
        this.capacityVolume = capacityVolume;
    }

    public int getMaxDeliveries() {
        return maxDeliveries;
    }

    public void setMaxDeliveries(int maxDeliveries) {
        this.maxDeliveries = maxDeliveries;
    }
}
