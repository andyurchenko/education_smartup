package edu.model;

import edu.model.additional.Passenger;
import edu.model.additional.TripDate;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private long id;
    private Trip trip;
    private TripDate tripDate;
    private List<Passenger> passengers;
    private BigDecimal totalPrice;
    private Client client;

    public Order() {
    }

    public Order(long id, Trip trip, TripDate tripDate, List<Passenger> passengers, BigDecimal totalPrice, Client client) {
        this.id = id;
        this.trip = trip;
        this.tripDate = tripDate;
        this.passengers = passengers;
        this.totalPrice = totalPrice;
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public TripDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(TripDate tripDate) {
        this.tripDate = tripDate;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
