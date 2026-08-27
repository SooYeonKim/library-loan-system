package com.library.loan.exception;

import com.library.loan.Loan;

import java.util.List;

public class OverdueLoanExistsException extends LoanNotAllowedException {
    public OverdueLoanExistsException(List<Loan> overdueLoans) {
        super("연체 중인 도서가 있어 대여할 수 없습니다.");
    }
}
