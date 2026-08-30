package com.library.loan;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.Period;

@Embeddable
public class LoanPeriod {
    private LocalDate startDate;
    private LocalDate dueDate;

    protected LoanPeriod() {}

    public LoanPeriod(LocalDate startDate, int loanDays) {
        this.startDate = startDate;
        this.dueDate = startDate.plusDays(loanDays);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdueAt(LocalDate date) {
        return date.isAfter(dueDate);
    }

    public int overdueDaysAt(LocalDate date) {
        if (!isOverdueAt(date)) {
            return 0;
        }
        return Period.between(dueDate, date).getDays();
    }
}
