package com.library.loan;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class LoanPeriodTest {

    private final Clock clock = Clock.fixed(
            Instant.parse("2026-08-26T00:00:00Z"), ZoneOffset.UTC
    );
    private LoanPeriod period;

    @Test
    void date가_dueDate와_같은_날이면_isOverdueAt는_false여야_한다() {
        // given
        period = new LoanPeriod(LocalDate.now(clock).minusDays(14), 14);

        // when
        boolean isOverdueAt = period.isOverdueAt(LocalDate.now(clock));

        // then
        assertThat(isOverdueAt).isFalse();
    }

    @Test
    void date가_dueDate_다음날이면_isOverdueAt는_true여야_한다() {
        // given
        period = new LoanPeriod(LocalDate.now(clock).minusDays(14 + 1), 14);

        // when
        boolean isOverdueAt = period.isOverdueAt(LocalDate.now(clock));

        // then
        assertThat(isOverdueAt).isTrue();
    }

    @Test
    void 연체가_아닌_경우_overdueDaysAt는_0을_반환해야_한다() {
        // given
        period = new LoanPeriod(LocalDate.now(clock).minusDays(14), 14);

        // when
        int overdueDaysAt = period.overdueDaysAt(LocalDate.now(clock));

        // then
        assertThat(overdueDaysAt).isEqualTo(0);
    }

    @Test
    void 연체인_경우_date가_dueDate보다_N일_늦으면_N을_반환해야_한다() {
        // given
        period = new LoanPeriod(LocalDate.now(clock).minusDays(14 + 5), 14);

        // when
        int overdueDaysAt = period.overdueDaysAt(LocalDate.now(clock));

        // then
        assertThat(overdueDaysAt).isEqualTo(5);
    }
}
