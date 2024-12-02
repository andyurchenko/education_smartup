package edu.dto.administrator;

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
import edu.util.validation.constraint.position.PositionEmptyConstraint;
import edu.util.validation.constraint.position.PositionMaxLengthConstraint;

import java.util.Objects;

public class DtoUpdateAdministratorRequest {
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

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String oldPassword;

    @PasswordEmptyConstraint
    @PasswordMinLengthConstraint
    private String newPassword;

    public DtoUpdateAdministratorRequest() {
    }

    public DtoUpdateAdministratorRequest(String firstName, String lastName, String patronymic, String position, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
        if (!(o instanceof DtoUpdateAdministratorRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getPatronymic(), that.getPatronymic()) && Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getOldPassword(), that.getOldPassword()) && Objects.equals(getNewPassword(), that.getNewPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPatronymic(), getPosition(), getOldPassword(), getNewPassword());
    }
}
