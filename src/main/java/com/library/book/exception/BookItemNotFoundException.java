package com.library.book.exception;

public class BookItemNotFoundException extends RuntimeException {
    public BookItemNotFoundException() {
        super("존재하지 않는 도서 사본입니다.");
    }
}
