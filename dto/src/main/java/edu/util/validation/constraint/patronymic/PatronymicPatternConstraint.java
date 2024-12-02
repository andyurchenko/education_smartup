package edu.util.validation.constraint.patronymic;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatronymicPatternConstraintValidator.class)
@Documented
public @interface PatronymicPatternConstraint {
    String message() default "";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_PATRONYMIC_PATTERN_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
