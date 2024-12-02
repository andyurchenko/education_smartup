package edu.util.validation.constraint.passport;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PassportEmptyConstraintValidator implements ConstraintValidator<PassportEmptyConstraint, String> {
    @Override
    public void initialize(PassportEmptyConstraint constraintAnnotation) {
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
