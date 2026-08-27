package com.library.loan.exception;

import com.library.book.BookItemStatus;

public class BookItemNotAvailableException extends RuntimeException {
    public BookItemNotAvailableException(BookItemStatus status) {
        super("대여할 수 없는 상태의 도서입니다: " + status);
    }
}
