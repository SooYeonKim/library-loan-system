package com.library.loan;

import com.library.book.BookItem;
import com.library.loan.exception.*;
import com.library.member.Member;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

public class LoanPolicy {
    private static final int MAX_LOAN_COUNT = 3;
    private static final int STANDARD_LOAN_DAYS = 14;

    private final Clock clock;

    public LoanPolicy(Clock clock) {
        this.clock = clock;
    }

    public Loan createLoan(Long memberId, BookItem bookItem,
                           List<Loan> activeLoans, List<Loan> returnedLoansWithPenalty) {
        LocalDate today = LocalDate.now(clock);

        if (!bookItem.isAvailable()) {
            throw new BookItemNotAvailableException(bookItem.getStatus());
        }
        if (activeLoans.size() >= MAX_LOAN_COUNT) {
            throw new MaxLoanExceededException();
        }
        List<Loan> overdueLoans = activeLoans.stream()
                .filter(loan -> loan.isOverdue(today))
                .toList();
        if (!overdueLoans.isEmpty()) {
            throw new OverdueLoanExistsException(overdueLoans);
        }
        List<Loan> penaltyLoans = returnedLoansWithPenalty.stream()
                .filter(loan -> loan.isPenaltyActive(today))
                .toList();
        if (!penaltyLoans.isEmpty()) {
            throw new PenaltyPeriodException(penaltyLoans);
        }

        LoanPeriod period = new LoanPeriod(today, STANDARD_LOAN_DAYS);
        Loan loan = new Loan(memberId, bookItem.getId(), period);
        bookItem.markAsLoaned();
        return loan;
    }
}
