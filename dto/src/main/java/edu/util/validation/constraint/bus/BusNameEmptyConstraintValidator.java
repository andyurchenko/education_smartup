package edu.util.validation.constraint.bus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BusNameEmptyConstraintValidator implements ConstraintValidator<BusNameEmptyConstraint, String> {
    @Override
    public void initialize(BusNameEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return false;
        }

        return true;
    }
}
