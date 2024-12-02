package edu.util.validation.constraint.trip.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateEmptyOrPatternConstraintValidator implements ConstraintValidator<DateEmptyOrPatternConstraint, String> {
    @Override
    public void initialize(DateEmptyOrPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^\\d{4}-((0[1-9])|(1[0-2]))-([012][0-9]|3[0-1])$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
