package edu.util.validation.constraint.email;

import edu.util.config.ValidationConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailPatternConstrainValidator implements ConstraintValidator<EmailPatternConstrain, String> {
    private final ValidationConfig validationConfig;

    @Autowired
    public EmailPatternConstrainValidator(ValidationConfig validationConfig) {
        this.validationConfig = validationConfig;
    }

    @Override
    public void initialize(EmailPatternConstrain constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank() || value.isEmpty()) {
            return true;
        }

        return value.matches(validationConfig.getEmailPattern());
    }
}
