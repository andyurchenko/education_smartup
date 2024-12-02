package edu.util.validation.constraint.trip.period;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodEmptyOrPatternConstraintValidator.class)
@Documented
public @interface PeriodEmptyOrPatternConstraint {
    String message() default "Period is empty or given in a wong way.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_PERIOD_EMPTY_OR_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
