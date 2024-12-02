package edu.model;

import java.util.Objects;

public class Bus {
    private long id;
    private String brandName;
    private int placeCount;

    public Bus() {
    }

    public Bus(long id, String brandName, int placeCount) {
        this.id = id;
        this.brandName = brandName;
        this.placeCount = placeCount;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bus bus)) return false;
        return getId() == bus.getId() && getPlaceCount() == bus.getPlaceCount() && Objects.equals(getBrandName(), bus.getBrandName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrandName(), getPlaceCount());
    }
}
