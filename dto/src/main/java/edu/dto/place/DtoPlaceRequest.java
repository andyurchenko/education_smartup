package edu.dto.place;

import edu.util.validation.constraint.firstname.FirstNameEmptyConstraint;
import edu.util.validation.constraint.lastname.LastNameEmptyConstraint;
import edu.util.validation.constraint.passport.PassportEmptyConstraint;

public class DtoPlaceRequest {
    private Long orderId;
    @FirstNameEmptyConstraint
    private String firstName;
    @LastNameEmptyConstraint
    private String lastName;
    @PassportEmptyConstraint
    private String passport;
    private Integer place;

    public DtoPlaceRequest() {
    }

    public DtoPlaceRequest(Long orderId, String firstName, String lastName, String passport, Integer place) {
        this.orderId = orderId;
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
