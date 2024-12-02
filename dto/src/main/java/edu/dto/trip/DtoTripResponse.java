package edu.dto.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.dto.trip.bus.DtoBusResponse;

import java.math.BigDecimal;
import java.util.List;

public class DtoTripResponse {
    private long tripId;
    private DtoBusResponse bus;
    private String fromStation;
    private String toStation;
    private String start;
    private String duration;
    private BigDecimal price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DtoSchedule schedule;
    private List<String> dates;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean approved;

    public DtoTripResponse() {
    }

    public DtoTripResponse(long tripId, DtoBusResponse bus, String fromStation, String toStation, String start, String duration, BigDecimal price, DtoSchedule schedule, List<String> dates, Boolean approved) {
        this.tripId = tripId;
        this.bus = bus;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.schedule = schedule;
        this.dates = dates;
        this.approved = approved;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public DtoBusResponse getBus() {
        return bus;
    }

    public void setBus(DtoBusResponse bus) {
        this.bus = bus;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DtoSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(DtoSchedule schedule) {
        this.schedule = schedule;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
