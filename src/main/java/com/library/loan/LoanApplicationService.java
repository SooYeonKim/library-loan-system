package com.library.loan;

import com.library.book.BookItem;
import com.library.book.BookItemRepository;
import com.library.book.exception.BookItemNotFoundException;
import com.library.member.MemberRepository;
import com.library.member.exception.MemberNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanApplicationService {

    private final MemberRepository memberRepository;
    private final BookItemRepository bookItemRepository;
    private final LoanRepository loanRepository;
    private final LoanPolicy loanPolicy;
    private final Clock clock;

    public LoanApplicationService(MemberRepository memberRepository,
                                  BookItemRepository bookItemRepository,
                                  LoanRepository loanRepository,
                                  LoanPolicy loanPolicy,
                                  Clock clock) {
        this.memberRepository = memberRepository;
        this.bookItemRepository = bookItemRepository;
        this.loanRepository = loanRepository;
        this.loanPolicy = loanPolicy;
        this.clock = clock;
    }

    @Transactional
    public Long loan(Long memberId, Long bookItemId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
        BookItem bookItem = bookItemRepository.findById(bookItemId)
                .orElseThrow(BookItemNotFoundException::new);

        List<Loan> activeLoans = loanRepository.findByMemberIdAndReturnedDateIsNull(memberId);
        List<Loan> returnedLoansWithPenalty = loanRepository
                .findByMemberIdAndPenaltyEndDateGreaterThanEqual(memberId, LocalDate.now(clock));

        Loan loan = loanPolicy.createLoan(memberId, bookItem, activeLoans, returnedLoansWithPenalty);

        loanRepository.save(loan);
        return loan.getId();
    }
}
