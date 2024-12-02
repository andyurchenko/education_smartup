package edu.util.validation.constraint.patronymic;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class PatronymicPatternConstraintValidator implements ConstraintValidator<PatronymicPatternConstraint, String>  {
    private final ValidationConfig validationConfig;

    @Autowired
    public PatronymicPatternConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(PatronymicPatternConstraint constraintAnnotation) {
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
