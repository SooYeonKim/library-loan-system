package com.library.loan;

import com.library.book.BookItem;
import com.library.book.BookItemStatus;
import com.library.loan.exception.*;
import com.library.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanPolicyTest {

    private final Clock clock = Clock.fixed(
            Instant.parse("2026-08-26T00:00:00Z"), ZoneOffset.UTC
    );
    private final LoanPolicy loanPolicy = new LoanPolicy(clock);
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "홍길동");
    }

    @Test
    void 조건을_모두_만족하면_대여가_생성된다() {
        // given
        BookItem bookItem = new BookItem(103L, 10L, BookItemStatus.AVAILABLE);
        List<Loan> activeLoans = List.of();
        List<Loan> returnedLoansWithPenalty = List.of();

        // when
        Loan loan = loanPolicy.createLoan(member, bookItem, activeLoans, returnedLoansWithPenalty);

        // then
        assertThat(loan.getMemberId()).isEqualTo(1L);
        assertThat(loan.getBookItemId()).isEqualTo(103L);
        assertThat(loan.getReturnedDate()).isNull();
        assertThat(loan.getPeriod().getStartDate()).isEqualTo(LocalDate.of(2026, 8, 26));
    }

    @Test
    void 최대_3권_초과_시_대여할_수_없다() {
        // given
        BookItem bookItem = new BookItem(104L, 13L, BookItemStatus.AVAILABLE);
        List<Loan> activeLoans = new ArrayList<>();
        activeLoans.add(new Loan(1L, 101L, new LoanPeriod(LocalDate.now(clock), 14)));
        activeLoans.add(new Loan(1L, 102L, new LoanPeriod(LocalDate.now(clock), 14)));
        activeLoans.add(new Loan(1L, 103L, new LoanPeriod(LocalDate.now(clock), 14)));
        List<Loan> returnedLoansWithPenalty = List.of();

        // when & then
        assertThatThrownBy(() -> loanPolicy.createLoan(member, bookItem, activeLoans, returnedLoansWithPenalty)).isInstanceOf(MaxLoanExceededException.class);
    }

    @Test
    void 연체_중인_대여가_있을_시_대여할_수_없다() {
        // given
        BookItem bookItem = new BookItem(102L, 13L, BookItemStatus.AVAILABLE);
        List<Loan> activeLoans = new ArrayList<>();
        activeLoans.add(new Loan(1L, 101L, new LoanPeriod(LocalDate.now(clock).minusDays(15), 14)));
        List<Loan> returnedLoansWithPenalty = List.of();

        // when & then
        assertThatThrownBy(() -> loanPolicy.createLoan(member, bookItem, activeLoans, returnedLoansWithPenalty)).isInstanceOf(OverdueLoanExistsException.class);
    }

    @Test
    void 페널티_기간_중일_시_대여할_수_없다() {
        // given
        BookItem bookItem = new BookItem(102L, 13L, BookItemStatus.AVAILABLE);
        List<Loan> activeLoans = List.of();
        List<Loan> returnedLoansWithPenalty = new ArrayList<>();
        Loan penaltyLoan = new Loan(1L, 105L, new LoanPeriod(LocalDate.now(clock).minusDays(20), 14));
        // 이 Loan은 시작일 20일 전, 14일 대여기간 → dueDate는 6일 전 → 이미 6일 연체 상태
        BookItem someItem = new BookItem(105L, 20L, BookItemStatus.LOANED);
        penaltyLoan.returnBook(someItem, LocalDate.now(clock));
        // 오늘 반납 처리 → 6일 연체 반납이니 penaltyEndDate = 오늘 + 6일
        returnedLoansWithPenalty.add(penaltyLoan);

        // when & then
        assertThatThrownBy(() -> loanPolicy.createLoan(member, bookItem, activeLoans, returnedLoansWithPenalty)).isInstanceOf(PenaltyPeriodException.class);
    }

    @Test
    void 책이_대여_불가_상태일_시_대여할_수_없다() {
        // given
        BookItem bookItem = new BookItem(101L, 13L, BookItemStatus.DAMAGED);
        List<Loan> activeLoans = List.of();
        List<Loan> returnedLoansWithPenalty = List.of();

        // when & then
        assertThatThrownBy(() -> loanPolicy.createLoan(member, bookItem, activeLoans, returnedLoansWithPenalty)).isInstanceOf(BookItemNotAvailableException.class);
    }
}
