package edu.model.additional;

import java.time.LocalDate;

public class Schedule {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String period;

    public Schedule() {
    }

    public Schedule(LocalDate fromDate, LocalDate toDate, String period) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.period = period;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
