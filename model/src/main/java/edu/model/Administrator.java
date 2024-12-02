package edu.model;

import edu.model.additional.UserType;

import java.util.Objects;

public class Administrator extends User {
    private String position;

    public Administrator() {
        super();
    }

    public Administrator(User user, String position) {
        super(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getUserType(),
                user.getLogin(),
                user.getPassword(),
                user.isAccountActive()
        );
        this.position = position;
    }

    public Administrator(long id, String firstName, String lastName, String patronymic, UserType userType, String login, String password, boolean accountActive, String position) {
        super(id, firstName, lastName, patronymic, userType, login, password, accountActive);
        this.position = position;
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
        if (!(o instanceof Administrator that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getPosition(), that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }
}
