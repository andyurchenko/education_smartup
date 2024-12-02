package edu.util.validation.constraint.lastname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LastNameEmptyConstraintValidator implements ConstraintValidator<LastNameEmptyConstraint, String> {
    @Override
    public void initialize(LastNameEmptyConstraint constraintAnnotation) {
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
