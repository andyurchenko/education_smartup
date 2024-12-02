package edu.dto.trip;

import edu.util.validation.constraint.bus.BusNameEmptyConstraint;
import edu.util.validation.constraint.trip.TripScheduleAndDatesConstraint;
import edu.util.validation.constraint.trip.date.DateEmptyOrPatternConstraint;
import edu.util.validation.constraint.trip.price.PriceEmptyOrPatternConstraint;
import edu.util.validation.constraint.trip.start.StartEmptyOrPatternConstraint;
import edu.util.validation.constraint.trip.station.StationEmptyConstraint;
import edu.util.validation.constraint.trip.duration.DurationEmptyOrPatternConstraint;
import jakarta.validation.Valid;
import java.util.List;

@TripScheduleAndDatesConstraint
public class DtoTripRequest {
    @BusNameEmptyConstraint
    private String busName;
    @StationEmptyConstraint
    private String fromStation;
    @StationEmptyConstraint
    private String toStation;
    @StartEmptyOrPatternConstraint
    private String start;
    @DurationEmptyOrPatternConstraint
    private String duration;
    @PriceEmptyOrPatternConstraint
    private String price;
    @Valid
    private DtoSchedule schedule;
    private List<@DateEmptyOrPatternConstraint String> dates;

    public DtoTripRequest() {
    }

    public DtoTripRequest(String busName, String fromStation, String toStation, String start, String duration, String price, DtoSchedule schedule, List<String> dates) {
        this.busName = busName;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.schedule = schedule;
        this.dates = dates;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public DtoSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(DtoSchedule schedule) {
        this.schedule = schedule;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}
