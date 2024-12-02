package edu.util.validation.constraint.trip.bus;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusPlaceCountConstraintValidator.class)
@Documented
public @interface BusPlaceCountConstraint {
    String message() default "Bus place count must have positive value.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_BUS_PLACE_COUNT_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
