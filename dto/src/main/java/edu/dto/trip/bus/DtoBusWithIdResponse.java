package edu.dto.trip.bus;

import java.util.Objects;

public class DtoBusWithIdResponse {
    private long id;
    private String busName;
    private int placeCount;

    public DtoBusWithIdResponse() {
    }

    public DtoBusWithIdResponse(long id, String busName, int placeCount) {
        this.id = id;
        this.busName = busName;
        this.placeCount = placeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        if (!(o instanceof DtoBusWithIdResponse that)) return false;
        return getId() == that.getId() && getPlaceCount() == that.getPlaceCount() && Objects.equals(getBusName(), that.getBusName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBusName(), getPlaceCount());
    }
}
