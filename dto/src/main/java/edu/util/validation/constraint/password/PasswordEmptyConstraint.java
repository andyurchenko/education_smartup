package edu.util.validation.constraint.password;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordEmptyConstraintValidator.class)
@Documented
public @interface PasswordEmptyConstraint {
    String message() default "";
    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_PASSWORD_EMPTY_CONSTRAINT;
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
