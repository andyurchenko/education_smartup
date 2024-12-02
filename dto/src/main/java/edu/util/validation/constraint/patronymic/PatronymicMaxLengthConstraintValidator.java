package edu.util.validation.constraint.patronymic;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PatronymicMaxLengthConstraintValidator implements ConstraintValidator<PatronymicMaxLengthConstraint, String> {
    private final ValidationConfig validationConfig;

    @Autowired
    public PatronymicMaxLengthConstraintValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(PatronymicMaxLengthConstraint constraintAnnotation) {
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
