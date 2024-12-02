package edu.dto.administrator;

import edu.util.validation.constraint.firstname.FirstNameEmptyConstraint;
import edu.util.validation.constraint.firstname.FirstNameMaxLengthConstraint;
import edu.util.validation.constraint.firstname.FirstNamePatternConstraint;
import edu.util.validation.constraint.lastname.LastNameEmptyConstraint;
import edu.util.validation.constraint.lastname.LastNameMaxLengthConstraint;
import edu.util.validation.constraint.lastname.LastNamePatternConstraint;
import edu.util.validation.constraint.login.LoginEmptyConstraint;
import edu.util.validation.constraint.login.LoginMaxLengthConstraint;
import edu.util.validation.constraint.login.LoginPatternConstraint;
import edu.util.validation.constraint.password.PasswordEmptyConstraint;
import edu.util.validation.constraint.password.PasswordMinLengthConstraint;
import edu.util.validation.constraint.patronymic.PatronymicMaxLengthConstraint;
import edu.util.validation.constraint.patronymic.PatronymicPatternConstraint;
import edu.util.validation.constraint.position.PositionEmptyConstraint;
import edu.util.validation.constraint.position.PositionMaxLengthConstraint;

import java.util.Objects;

public class DtoRegisterNewAdministratorRequest {
    @FirstNameEmptyConstraint
    @FirstNamePatternConstraint
    @FirstNameMaxLengthConstraint
    private String firstName;

    @LastNameEmptyConstraint
    @LastNamePatternConstraint
    @LastNameMaxLengthConstraint
    private String lastName;

    @PatronymicMaxLengthConstraint
    @PatronymicPatternConstraint
    private String patronymic;
    @PositionEmptyConstraint
    @PositionMaxLengthConstraint
    private String position;

    @LoginEmptyConstraint
    @LoginMaxLengthConstraint
    @LoginPatternConstraint
    private String login;

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String password;

    public DtoRegisterNewAdministratorRequest() {
    }

    public DtoRegisterNewAdministratorRequest(String firstName, String lastName, String patronymic, String position, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.login = login;
        this.password = password;
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
        if (!(o instanceof DtoRegisterNewAdministratorRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName())
                && Objects.equals(getLastName(), that.getLastName())
                && Objects.equals(getPatronymic(), that.getPatronymic())
                && Objects.equals(getPosition(), that.getPosition())
                && Objects.equals(getLogin(), that.getLogin())
                && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic(), getPosition(), getLogin(), getPassword());
    }
}
