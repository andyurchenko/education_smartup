package edu.util.validation.constraint.trip.start;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartEmptyPatternConstraintValidator.class)
@Documented
public @interface StartEmptyOrPatternConstraint {
    String message() default "Start time is empty or given in a wrong way.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_START_TIME_EMPTY_OR_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
