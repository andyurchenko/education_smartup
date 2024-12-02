package edu.dto.order;

import java.util.List;

public class DtoOrderResponse {
    private long orderId;
    private long tripId;
    private String fromStation;
    private String toStation;
    private String busName;
    private String date;
    private String start;
    private String duration;
    private String price;
    private String totalPrice;
    List<DtoPassenger> passengers;

    public DtoOrderResponse() {
    }

    public DtoOrderResponse(long orderId, long tripId, String fromStation, String toStation, String busName, String date, String start, String duration, String price, String totalPrice, List<DtoPassenger> passengers) {
        this.orderId = orderId;
        this.tripId = tripId;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.busName = busName;
        this.date = date;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.totalPrice = totalPrice;
        this.passengers = passengers;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
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

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<DtoPassenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<DtoPassenger> passengers) {
        this.passengers = passengers;
    }
}
