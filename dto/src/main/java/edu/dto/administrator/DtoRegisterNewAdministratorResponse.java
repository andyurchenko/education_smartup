package edu.dto.administrator;

import edu.model.additional.UserType;

import java.util.Objects;

public class DtoRegisterNewAdministratorResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private UserType userType;

    public DtoRegisterNewAdministratorResponse() {
    }

    public DtoRegisterNewAdministratorResponse(long id, String firstName, String lastName, String patronymic, String position, UserType userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.userType = userType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoRegisterNewAdministratorResponse that)) return false;
        return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getPatronymic(), that.getPatronymic()) && Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getUserType(), that.getUserType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getPosition(), getUserType());
    }
}
