package edu.dto.user;

import edu.util.validation.constraint.login.LoginEmptyConstraint;
import edu.util.validation.constraint.password.PasswordEmptyConstraint;

import java.util.Objects;

public class DtoLogin {
    @LoginEmptyConstraint
    private String login;
    @PasswordEmptyConstraint
    private String password;

    public DtoLogin() {
    }

    public DtoLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoLogin dtoLogin)) return false;
        return Objects.equals(getLogin(), dtoLogin.getLogin()) && Objects.equals(getPassword(), dtoLogin.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPassword());
    }
}
