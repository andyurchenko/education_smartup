package edu.util.validation.constraint.login;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginMaxLengthConstraintValidator implements ConstraintValidator<LoginMaxLengthConstraint, String> {
    private final ValidationConfig validationConfig;

    @Autowired
    public LoginMaxLengthConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(LoginMaxLengthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return true;
        }

        return value.length() <= validationConfig.getMaxStringLength();
    }
}
