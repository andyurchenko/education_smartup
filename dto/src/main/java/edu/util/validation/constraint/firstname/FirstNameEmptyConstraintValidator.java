package edu.util.validation.constraint.firstname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FirstNameEmptyConstraintValidator implements ConstraintValidator<FirstNameEmptyConstraint, String> {
    @Override
    public void initialize(FirstNameEmptyConstraint constraintAnnotation) {
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
