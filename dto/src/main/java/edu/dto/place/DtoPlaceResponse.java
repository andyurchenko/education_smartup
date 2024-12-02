package edu.dto.place;

public class DtoPlaceResponse {
    private Long orderId;
    private String ticket;
    private String firstName;
    private String lastName;
    private String passport;
    private Integer place;

    public DtoPlaceResponse() {
    }

    public DtoPlaceResponse(Long orderId, String ticket, String firstName, String lastName, String passport, Integer place) {
        this.orderId = orderId;
        this.ticket = ticket;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.place = place;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }
}
