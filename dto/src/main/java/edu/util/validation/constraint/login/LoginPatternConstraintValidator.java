package edu.util.validation.constraint.login;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.regex.Pattern;

public class LoginPatternConstraintValidator implements ConstraintValidator<LoginPatternConstraint, String>  {
    private final ValidationConfig validationConfig;

    @Autowired
    public LoginPatternConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(LoginPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return true;
        }

        return Pattern.matches(validationConfig.getLoginPattern(), value);
    }
}
