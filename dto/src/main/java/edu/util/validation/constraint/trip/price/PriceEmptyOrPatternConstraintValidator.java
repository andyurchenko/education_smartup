package edu.util.validation.constraint.trip.price;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceEmptyOrPatternConstraintValidator implements ConstraintValidator<PriceEmptyOrPatternConstraint, String> {
    @Override
    public void initialize(PriceEmptyOrPatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        return value.matches("^\\d+\\.\\d{2}$");
    }
}
