package edu.dto.trip.bus;

import java.util.Objects;

public class DtoBusResponse {
    private String busName;
    private int placeCount;

    public DtoBusResponse() {
    }

    public DtoBusResponse(String busName, int placeCount) {
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
        if (!(o instanceof DtoBusResponse that)) return false;
        return getPlaceCount() == that.getPlaceCount() && Objects.equals(getBusName(), that.getBusName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBusName(), getPlaceCount());
    }
}
