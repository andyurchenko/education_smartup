package edu.util.validation.constraint.firstname;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class FirstNamePatternConstraintValidator implements ConstraintValidator<FirstNamePatternConstraint, String>  {
    private final ValidationConfig validationConfig;

    @Autowired
    public FirstNamePatternConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(FirstNamePatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return true;
        }

        return Pattern.matches(validationConfig.getFullNamePattern(), value);
    }
}
