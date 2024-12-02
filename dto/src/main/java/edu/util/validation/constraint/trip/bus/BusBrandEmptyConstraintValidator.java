package edu.util.validation.constraint.trip.bus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BusBrandEmptyConstraintValidator implements ConstraintValidator<BusBrandNameEmptyConstraint, String> {
    @Override
    public void initialize(BusBrandNameEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        return true;
    }
}
