package com.library.loan.exception;

import com.library.loan.Loan;

import java.util.List;

public class PenaltyPeriodException extends LoanNotAllowedException {
    public PenaltyPeriodException(List<Loan> activePenaltyLoans) {
        super("연체 페널티 기간입니다.");
    }
}
