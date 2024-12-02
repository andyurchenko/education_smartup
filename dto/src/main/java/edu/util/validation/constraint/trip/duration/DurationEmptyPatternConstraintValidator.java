package edu.util.validation.constraint.trip.duration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationEmptyPatternConstraintValidator implements ConstraintValidator<DurationEmptyOrPatternConstraint, String> {
    @Override
    public void initialize(DurationEmptyOrPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(\\d{2,3}):([0-5]\\d)$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
