package com.example.deliveryoptimizer.dto;

/**
 * Simple DTO for Delivery entity used to expose data to clients.
 */
public class DeliveryDto {
    private Long id;
    private double latitude;
    private double longitude;
    private double weight;
    private double volume;
    private String status;

    public DeliveryDto() {
    }

    public DeliveryDto(Long id, double latitude, double longitude, double weight, double volume, String status) {
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
}
