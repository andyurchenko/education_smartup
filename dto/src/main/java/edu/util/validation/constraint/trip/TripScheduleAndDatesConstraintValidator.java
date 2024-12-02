package edu.util.validation.constraint.trip;

import edu.dto.trip.DtoTripRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TripScheduleAndDatesConstraintValidator implements ConstraintValidator<TripScheduleAndDatesConstraint, DtoTripRequest>  {
    @Override
    public void initialize(TripScheduleAndDatesConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DtoTripRequest dto, ConstraintValidatorContext context) {

        if (dto.getSchedule() != null && dto.getDates() != null) {
            return false;
        }

        if (dto.getSchedule() == null && dto.getDates() == null) {
            return false;
        }

        return true;
    }
}
