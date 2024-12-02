package edu.dto.client;

import edu.util.validation.constraint.email.EmailEmptyConstraint;
import edu.util.validation.constraint.email.EmailPatternConstrain;
import edu.util.validation.constraint.firstname.FirstNameEmptyConstraint;
import edu.util.validation.constraint.firstname.FirstNameMaxLengthConstraint;
import edu.util.validation.constraint.firstname.FirstNamePatternConstraint;
import edu.util.validation.constraint.lastname.LastNameEmptyConstraint;
import edu.util.validation.constraint.lastname.LastNameMaxLengthConstraint;
import edu.util.validation.constraint.lastname.LastNamePatternConstraint;
import edu.util.validation.constraint.password.PasswordEmptyConstraint;
import edu.util.validation.constraint.password.PasswordMinLengthConstraint;
import edu.util.validation.constraint.patronymic.PatronymicMaxLengthConstraint;
import edu.util.validation.constraint.patronymic.PatronymicPatternConstraint;
import edu.util.validation.constraint.phone.PhoneEmptyConstraint;
import edu.util.validation.constraint.phone.PhonePatternConstrain;
import java.util.Objects;

public class DtoUpdateClientRequest {
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

    @EmailEmptyConstraint
    @EmailPatternConstrain
    private String email;

    @PhoneEmptyConstraint
    @PhonePatternConstrain
    private String phone;

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String oldPassword;

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String newPassword;

    public DtoUpdateClientRequest() {
    }

    public DtoUpdateClientRequest(String firstName, String lastName, String patronymic, String email, String phone, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoUpdateClientRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getPatronymic(), that.getPatronymic()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPhone(), that.getPhone()) && Objects.equals(getOldPassword(), that.getOldPassword()) && Objects.equals(getNewPassword(), that.getNewPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic(), getEmail(), getPhone(), getOldPassword(), getNewPassword());
    }
}
