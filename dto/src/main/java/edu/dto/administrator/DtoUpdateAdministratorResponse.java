package edu.dto.administrator;

import edu.model.additional.UserType;

import java.util.Objects;

public class DtoUpdateAdministratorResponse {
    private String firstName;
    private String lastName;
    private String patronymic;
    private UserType userType;
    private String position;

    public DtoUpdateAdministratorResponse() {
    }

    public DtoUpdateAdministratorResponse(String firstName, String lastName, String patronymic, UserType userType, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.userType = userType;
        this.position = position;
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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoUpdateAdministratorResponse that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getPatronymic(), that.getPatronymic()) && getUserType() == that.getUserType() && Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic(), getUserType(), getPosition());
    }
}
