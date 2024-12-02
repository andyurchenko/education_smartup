package edu.util.validation.constraint.trip.bus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BusPlaceCountConstraintValidator implements ConstraintValidator<BusPlaceCountConstraint, Integer> {
    @Override
    public void initialize(BusPlaceCountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null || value <= 0 ) {
            return false;
        }

        return true;
    }
}
