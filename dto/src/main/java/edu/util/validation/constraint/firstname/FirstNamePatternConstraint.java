package edu.util.validation.constraint.firstname;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FirstNamePatternConstraintValidator.class)
@Documented
public @interface FirstNamePatternConstraint {
    String message() default "";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_FIRST_NAME_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
