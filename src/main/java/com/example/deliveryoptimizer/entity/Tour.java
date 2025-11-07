package com.example.deliveryoptimizer.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tour entity: represents a delivery tour (one vehicle on a given date).
 * Relations:
 * - ManyToOne to Vehicle (a tour uses one vehicle)
 * - OneToMany to Delivery (a tour has multiple deliveries)
 */
@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    public Tour() {
    }

    public Tour(LocalDate date, Vehicle vehicle) {
        this.date = date;
        this.vehicle = vehicle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    /**
     * Helper to add a delivery and set the back-reference.
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setTour(this);
    }

    /**
     * Helper to remove a delivery and clear the back-reference.
     */
    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
        delivery.setTour(null);
    }
}
