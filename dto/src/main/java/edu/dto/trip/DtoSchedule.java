package edu.dto.trip;

import edu.util.validation.constraint.trip.date.DateEmptyOrPatternConstraint;
import edu.util.validation.constraint.trip.period.PeriodEmptyOrPatternConstraint;
import java.util.Objects;

public class DtoSchedule {
    @DateEmptyOrPatternConstraint
    private String fromDate;
    @DateEmptyOrPatternConstraint
    private String toDate;
    @PeriodEmptyOrPatternConstraint
    private String period;

    public DtoSchedule() {
    }

    public DtoSchedule(String fromDate, String toDate, String period) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.period = period;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoSchedule that)) return false;
        return Objects.equals(getFromDate(), that.getFromDate()) && Objects.equals(getToDate(), that.getToDate()) && Objects.equals(getPeriod(), that.getPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromDate(), getToDate(), getPeriod());
    }
}
