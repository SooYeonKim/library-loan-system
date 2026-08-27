package com.library.loan.exception;

public abstract class LoanNotAllowedException extends RuntimeException {
    protected LoanNotAllowedException(String message) {
        super(message);
    }
}
