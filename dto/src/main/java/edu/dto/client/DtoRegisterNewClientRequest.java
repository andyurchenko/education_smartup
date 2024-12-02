package edu.dto.client;

import edu.util.validation.constraint.email.EmailEmptyConstraint;
import edu.util.validation.constraint.email.EmailPatternConstrain;
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
import edu.util.validation.constraint.phone.PhoneEmptyConstraint;
import edu.util.validation.constraint.phone.PhonePatternConstrain;

import java.util.Objects;

public class DtoRegisterNewClientRequest {
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

    @LoginEmptyConstraint
    @LoginMaxLengthConstraint
    @LoginPatternConstraint
    private String login;

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String password;
    @EmailEmptyConstraint
    @EmailPatternConstrain
    private String email;
    @PhoneEmptyConstraint
    @PhonePatternConstrain
    private String phone;

    public DtoRegisterNewClientRequest() {
    }

    public DtoRegisterNewClientRequest(String firstName, String lastName, String patronymic, String email, String phone, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
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
        if (!(o instanceof DtoRegisterNewClientRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getPatronymic(), that.getPatronymic()) && Objects.equals(getLogin(), that.getLogin()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPhone(), that.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic(), getLogin(), getPassword(), getEmail(), getPhone());
    }
}