package edu.util.validation.constraint.login;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginMaxLengthConstraintValidator.class)
@Documented
public @interface LoginMaxLengthConstraint {
    String message() default "";

    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_LOGIN_MAX_LENGTH_CONSTRAINT;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
