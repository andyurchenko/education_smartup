package edu.util.validation.constraint.phone;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhonePatternConstrainValidator.class)
@Documented
public @interface PhonePatternConstrain {
    String message() default "";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_PHONE_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
