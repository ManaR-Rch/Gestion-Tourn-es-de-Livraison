package com.example.deliveryoptimizer.entity;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "delivery_histories")
public class DeliveryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private LocalDate date;

    private LocalTime plannedTime;

    private LocalTime actualTime;

    // delay in minutes (can be negative if early)
    private Long delay;

    private DayOfWeek dayOfWeek;

    public DeliveryHistory() {
    }

    public DeliveryHistory(Customer customer, Tour tour, LocalDate date, LocalTime plannedTime, LocalTime actualTime, Long delay, DayOfWeek dayOfWeek) {
        this.customer = customer;
        this.tour = tour;
        this.date = date;
        this.plannedTime = plannedTime;
        this.actualTime = actualTime;
        this.delay = delay;
        this.dayOfWeek = dayOfWeek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(LocalTime plannedTime) {
        this.plannedTime = plannedTime;
    }

    public LocalTime getActualTime() {
        return actualTime;
    }

    public void setActualTime(LocalTime actualTime) {
        this.actualTime = actualTime;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
