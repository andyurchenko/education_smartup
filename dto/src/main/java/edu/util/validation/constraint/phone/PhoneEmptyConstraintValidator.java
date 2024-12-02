package edu.util.validation.constraint.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneEmptyConstraintValidator implements ConstraintValidator<PhoneEmptyConstraint, String> {
    @Override
    public void initialize(PhoneEmptyConstraint constraintAnnotation) {
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
