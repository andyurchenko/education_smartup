package edu.util.validation.constraint.trip.bus;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusBrandEmptyConstraintValidator.class)
@Documented
public @interface BusBrandNameEmptyConstraint {
    String message() default "Bus brand name is empty.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_BUS_NAME_EMPTY_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
