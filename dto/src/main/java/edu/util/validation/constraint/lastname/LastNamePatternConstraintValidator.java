package edu.util.validation.constraint.lastname;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class LastNamePatternConstraintValidator implements ConstraintValidator<LastNamePatternConstraint, String>  {
    private final ValidationConfig validationConfig;

    @Autowired
    public LastNamePatternConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(LastNamePatternConstraint constraintAnnotation) {
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
