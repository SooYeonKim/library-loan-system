package com.library.book;

public class BookItem {
    private final Long id;
    private final Long bookId;
    private BookItemStatus status; // AVAILABLE, LOANED, LOST, DAMAGED

    public BookItem(Long id, Long bookId, BookItemStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public BookItemStatus getStatus() {
        return status;
    }

    public void markAsLoaned() {
        this.status = BookItemStatus.LOANED;
    }

    public void markAsAvailable() {
        this.status = BookItemStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == BookItemStatus.AVAILABLE;
    }
}
