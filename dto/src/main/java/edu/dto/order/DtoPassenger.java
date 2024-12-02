package edu.dto.order;

import edu.util.validation.constraint.firstname.FirstNameEmptyConstraint;
import edu.util.validation.constraint.lastname.LastNameEmptyConstraint;
import edu.util.validation.constraint.passport.PassportEmptyConstraint;

public class DtoPassenger {
    @FirstNameEmptyConstraint
    private String firstName;
    @LastNameEmptyConstraint
    private String lastName;
    @PassportEmptyConstraint
    private String passport;

    public DtoPassenger() {
    }

    public DtoPassenger(String firstName, String lastName, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
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
}
