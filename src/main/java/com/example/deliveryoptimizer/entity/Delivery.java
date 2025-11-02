package com.example.deliveryoptimizer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;

    private double longitude;

    private double weight;

    private double volume;

    private String status;
    
    // Many-to-one relation: a delivery can belong to one Tour (route)
    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "tour_id")
    private Tour tour;

    public Delivery() {
    }

    /**
     * Constructor without id (used when creating new Delivery objects before persisting).
     */
    public Delivery(double latitude, double longitude, double weight, double volume, String status) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weight = weight;
        this.volume = volume;
        this.status = status;
    }

    /**
     * Full constructor (with id) for convenience (e.g., tests).
     */
    public Delivery(Long id, double latitude, double longitude, double weight, double volume, String status) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weight = weight;
        this.volume = volume;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }
}
