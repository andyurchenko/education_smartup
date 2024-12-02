package edu.util.validation.constraint.bus;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BusNameEmptyConstraintValidator.class)
@Documented
public @interface BusNameEmptyConstraint {
    String message() default "";
    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_BUS_NAME_EMPTY_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
