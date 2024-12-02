package edu.util.validation.constraint.position;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PositionMaxLengthConstraintValidator implements ConstraintValidator<PositionMaxLengthConstraint, String> {
    private final ValidationConfig validationConfig;

    @Autowired
    public PositionMaxLengthConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(PositionMaxLengthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return true;
        }

        return value.length() <= validationConfig.getMaxStringLength();
    }
}
