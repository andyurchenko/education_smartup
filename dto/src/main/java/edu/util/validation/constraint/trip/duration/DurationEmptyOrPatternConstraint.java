package edu.util.validation.constraint.trip.duration;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationEmptyPatternConstraintValidator.class)
@Documented
public @interface DurationEmptyOrPatternConstraint {
    String message() default "Duration time is empty or given in a wrong way.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_DURATION_TIME_EMPTY_OR_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
