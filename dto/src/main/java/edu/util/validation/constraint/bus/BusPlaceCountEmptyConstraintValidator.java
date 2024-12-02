package edu.util.validation.constraint.bus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BusPlaceCountEmptyConstraintValidator implements ConstraintValidator<BusPlaceCountEmptyConstraint, Integer> {
    @Override
    public void initialize(BusPlaceCountEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value > 0;
    }
}
