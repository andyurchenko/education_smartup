package edu.util.validation.constraint.lastname;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LastNameEmptyConstraintValidator.class)
@Documented
public @interface LastNameEmptyConstraint {
    String message() default "";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_LAST_NAME_EMPTY_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
