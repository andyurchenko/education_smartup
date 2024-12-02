package edu.util.validation.constraint.trip.start;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartEmptyPatternConstraintValidator implements ConstraintValidator<StartEmptyOrPatternConstraint, String> {
    @Override
    public void initialize(StartEmptyOrPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(([01]\\d)|2[0-3]):([0-5]\\d)$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
