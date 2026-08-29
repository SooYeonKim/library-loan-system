package com.library.loan;

import com.library.book.BookItem;
import com.library.book.BookItemStatus;
import com.library.loan.exception.AlreadyReturnedException;
import com.library.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanTest {

    private final Clock clock = Clock.fixed(
            Instant.parse("2026-08-26T00:00:00Z"), ZoneOffset.UTC
    );
    private BookItem bookItem;
    private Loan loan;

    @BeforeEach
    void setUp() {
        bookItem = new BookItem(101L, 10L, BookItemStatus.LOANED);
    }

    @Test
    void 정상_반납_시_returnedDate가_설정되고_bookItem이_AVAILABLE이_되고_penaltyEndDate는_null이다() {
        // given
        loan = new Loan(1L, 101L, new LoanPeriod(LocalDate.now(clock), 14));

        // when
        loan.returnBook(bookItem, LocalDate.now(clock));

        // then
        assertThat(loan.getReturnedDate()).isEqualTo(LocalDate.now(clock));
        assertThat(bookItem.getStatus()).isEqualTo(BookItemStatus.AVAILABLE);
        assertThat(loan.getPenaltyEndDate()).isNull();
    }

    @Test
    void 연체_상태로_반납_시_penaltyEndDate가_연체일_만큼_계산된다() {
        // given
        loan = new Loan(1L, 101L, new LoanPeriod(LocalDate.now(clock).minusDays(15), 14));

        // when
        loan.returnBook(bookItem, LocalDate.now(clock));

        // then
        assertThat(loan.getReturnedDate()).isEqualTo(LocalDate.now(clock));
        assertThat(bookItem.getStatus()).isEqualTo(BookItemStatus.AVAILABLE);
        assertThat(loan.getPenaltyEndDate()).isEqualTo(LocalDate.now(clock).plusDays(1));
    }

    @Test
    void 이미_반납된_도서_반납_시_예외가_발생한다() {
        // given
        loan = new Loan(1L, 101L, new LoanPeriod(LocalDate.now(clock), 14));
        loan.returnBook(bookItem, LocalDate.now(clock));

        // when & then
        assertThatThrownBy(() -> loan.returnBook(bookItem, LocalDate.now(clock))).isInstanceOf(AlreadyReturnedException.class);
    }
}
