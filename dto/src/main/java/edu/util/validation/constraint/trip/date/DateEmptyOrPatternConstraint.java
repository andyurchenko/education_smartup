package edu.util.validation.constraint.trip.date;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateEmptyOrPatternConstraintValidator.class)
@Documented
public @interface DateEmptyOrPatternConstraint {
    String message() default "Date is empty or given in a wong way.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_DATE_PATTERN_OR_EMPTY_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
