package com.library.loan.exception;

public class AlreadyReturnedException extends RuntimeException {
    public AlreadyReturnedException() {
        super("이미 반납된 도서입니다.");
    }
}
