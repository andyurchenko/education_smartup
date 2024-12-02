package edu.dto.trip.station;

import edu.util.validation.constraint.trip.station.StationEmptyConstraint;

public class DtoStationRequest {
    @StationEmptyConstraint
    private String name;

    public DtoStationRequest() {
    }

    public DtoStationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
