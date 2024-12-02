package edu.dto.order;

import edu.util.validation.constraint.trip.date.DateEmptyOrPatternConstraint;
import jakarta.validation.Valid;

import java.util.List;

public class DtoOrderRequest {
    private long tripId;
    @DateEmptyOrPatternConstraint
    private String date;
    List<@Valid DtoPassenger> passengers;

    public DtoOrderRequest() {
    }

    public DtoOrderRequest(long tripId, String date, List<DtoPassenger> passengers) {
        this.tripId = tripId;
        this.date = date;
        this.passengers = passengers;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DtoPassenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<DtoPassenger> passengers) {
        this.passengers = passengers;
    }
}
