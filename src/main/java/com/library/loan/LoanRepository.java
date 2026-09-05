package com.library.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberIdAndReturnedDateIsNull(Long memberId);

    List<Loan> findByMemberIdAndPenaltyEndDateGreaterThanEqual(Long memberId, LocalDate date);
}
