package com.library.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class BookItem {
    @Id @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private Long bookId;
    private BookItemStatus status; // AVAILABLE, LOANED, LOST, DAMAGED

    protected BookItem() {}

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
