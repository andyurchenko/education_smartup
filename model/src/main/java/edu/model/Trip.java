package edu.model;

import edu.model.additional.Schedule;
import edu.model.additional.Station;
import edu.model.additional.TripDate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class Trip {
    private long id;
    private Bus bus;
    private Station fromStation;
    private Station toStation;
    private LocalTime start;
    private Duration duration;
    private BigDecimal price;
    private List<TripDate> tripDates;
    private Schedule schedule;
    private boolean approved;

    public Trip() {
    }

    public Trip(long id, Bus bus, Station fromStation, Station toStation, LocalTime start, Duration duration, BigDecimal price, List<TripDate> tripDates, Schedule schedule, boolean approved) {
        this.id = id;
        this.bus = bus;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.tripDates = tripDates;
        this.schedule = schedule;
        this.approved = approved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<TripDate> getTripDates() {
        return tripDates;
    }

    public void setTripDates(List<TripDate> tripDates) {
        this.tripDates = tripDates;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
