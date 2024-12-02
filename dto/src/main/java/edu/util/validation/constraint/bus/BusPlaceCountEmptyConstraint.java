package edu.util.validation.constraint.bus;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusPlaceCountEmptyConstraintValidator.class)
@Documented
public @interface BusPlaceCountEmptyConstraint {
    String message() default "";
    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_BUS_PLACE_COUNT_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
