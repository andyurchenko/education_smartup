package edu.model.additional;

import edu.model.Trip;

import java.time.LocalDate;

public class TripDate {
    private long id;
    private LocalDate date;
    private Trip trip;
    private int freePassengerSeatsCounter;

    public TripDate() {
    }

    public TripDate(long id, LocalDate date, Trip trip, int freePassengerSeatsCounter) {
        this.id = id;
        this.date = date;
        this.trip = trip;
        this.freePassengerSeatsCounter = freePassengerSeatsCounter;
    }

    public TripDate(LocalDate date, Trip trip) {
        this.id = 0;
        this.date = date;
        this.trip = trip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getFreePassengerSeatsCounter() {
        return freePassengerSeatsCounter;
    }

    public void setFreePassengerSeatsCounter(int freePassengerSeatsCounter) {
        this.freePassengerSeatsCounter = freePassengerSeatsCounter;
    }
}
