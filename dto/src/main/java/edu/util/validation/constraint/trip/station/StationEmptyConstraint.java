package edu.util.validation.constraint.trip.station;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StationEmptyConstraintValidator.class)
@Documented
public @interface StationEmptyConstraint {
    String message() default "Station name is empty.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_STATION_EMPTY_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
