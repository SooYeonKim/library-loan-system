package com.library.loan.exception;

public class MaxLoanExceededException extends LoanNotAllowedException {
    public MaxLoanExceededException() {
        super("최대 대여 건수를 초과하였습니다.");
    }
}
