package edu.model;

import edu.model.additional.UserType;

import java.util.Objects;

public class Client extends User {
    private String email;
    private String phone;

    public Client() {
        super();
    }

    public Client(long id, String firstName, String lastName, String patronymic, UserType userType, String login, String password, boolean accountActive, String email, String phone) {
        super(id, firstName, lastName, patronymic, userType, login, password, accountActive);
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getEmail(), client.getEmail()) && Objects.equals(getPhone(), client.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail(), getPhone());
    }
}
