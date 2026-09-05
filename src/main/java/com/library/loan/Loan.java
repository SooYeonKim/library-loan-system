package com.library.loan;

import com.library.book.BookItem;
import com.library.loan.exception.AlreadyReturnedException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private LoanPeriod period;

    private Long memberId;
    private Long bookItemId;
    private LocalDate returnedDate;
    private LocalDate penaltyEndDate;

    protected Loan() {}

    Loan(Long memberId, Long bookItemId, LoanPeriod period) {
        this.memberId = memberId;
        this.bookItemId = bookItemId;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getBookItemId() {
        return bookItemId;
    }

    public LoanPeriod getPeriod() {
        return period;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public LocalDate getPenaltyEndDate() {
        return penaltyEndDate;
    }

    public boolean isOverdue(LocalDate today) {
        return returnedDate == null && period.isOverdueAt(today);
    }

    public boolean isPenaltyActive(LocalDate today) {
        return penaltyEndDate != null && !today.isAfter(penaltyEndDate);
    }

    public void returnBook(BookItem bookItem, LocalDate today) {
        if (this.returnedDate != null) {
            throw new AlreadyReturnedException();
        }
        this.returnedDate = today;
        if (period.isOverdueAt(today)) {
            int overdueDays = period.overdueDaysAt(today);
            this.penaltyEndDate = today.plusDays(overdueDays);
        }
        bookItem.markAsAvailable();
    }
}
