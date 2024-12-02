package edu.util.validation.constraint.trip.price;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceEmptyOrPatternConstraintValidator.class)
@Documented
public @interface PriceEmptyOrPatternConstraint {
    String message() default "Price is empty or given in a wrong way.";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_PRICE_EMPTY_OR_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
