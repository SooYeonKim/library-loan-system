package com.library.loan;

import com.library.book.BookItem;

import java.time.LocalDate;

public class Loan {
    private final Long memberId;
    private final Long bookItemId;
    private final LoanPeriod period;
    private LocalDate returnedDate;
    private LocalDate penaltyEndDate;

    Loan(Long memberId, Long bookItemId, LoanPeriod period) {
        this.memberId = memberId;
        this.bookItemId = bookItemId;
        this.period = period;
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
            throw new IllegalStateException("이미 반납되었습니다.");
        }
        this.returnedDate = today;
        if (period.isOverdueAt(today)) {
            int overdueDays = period.overdueDaysAt(today);
            this.penaltyEndDate = today.plusDays(overdueDays);
        }
        bookItem.markAsAvailable();
    }
}
