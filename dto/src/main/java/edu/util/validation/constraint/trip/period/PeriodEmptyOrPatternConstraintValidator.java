package edu.util.validation.constraint.trip.period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeriodEmptyOrPatternConstraintValidator implements ConstraintValidator<PeriodEmptyOrPatternConstraint, String> {
    @Override
    public void initialize(PeriodEmptyOrPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        String period = value;
        period = period.replaceAll("\\s", "").toLowerCase();

        if (period.equals("daily") || period.equals("even") || period.equals("odd")) {
            return true;
        }

        if (period.matches("^(([1-2][0-9])|(3[0-1])|(0[1-9])|([1-9](?!\\d))|,)+$")) {
            return true;
        }

        if (period.matches("^((sun(?=,|\\s))|(mon(?=,|\\s))|(tue(?=,|\\s))|(wed(?=,|\\s))|(thu(?=,|\\s))|(fri(?=,|\\s))|(sat(?=,|\\s))|,)+(sun|mon|tue|wed|thu|fri|sat)$")) {
            return true;

        }

        return false;
    }
}
