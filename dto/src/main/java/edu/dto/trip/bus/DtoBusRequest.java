package edu.dto.trip.bus;

import edu.util.validation.constraint.bus.BusNameEmptyConstraint;
import edu.util.validation.constraint.bus.BusPlaceCountEmptyConstraint;

import java.util.Objects;

public class DtoBusRequest {
    @BusNameEmptyConstraint
    private String busName;
    @BusPlaceCountEmptyConstraint
    private int placeCount;

    public DtoBusRequest() {
    }

    public DtoBusRequest(String busName, int placeCount) {
        this.busName = busName;
        this.placeCount = placeCount;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoBusRequest that)) return false;
        return getPlaceCount() == that.getPlaceCount() && Objects.equals(getBusName(), that.getBusName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBusName(), getPlaceCount());
    }
}
