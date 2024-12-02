package edu.util.validation.constraint.trip;

import edu.error.ApplicationErrorDetails;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TripScheduleAndDatesConstraintValidator.class})
public @interface TripScheduleAndDatesConstraint {
    String message() default "You cannot provide a schedule and trip dates in the same time.";
    Class<?>[] groups() default {};
    ApplicationErrorDetails errorDescription() default ApplicationErrorDetails.VIOLATION_SCHEDULE_AND_DATES_TOGETHER_PRESENT_OR_EMPTY_CONSTRAINT;
    Class<? extends Payload>[] payload() default {};
}

