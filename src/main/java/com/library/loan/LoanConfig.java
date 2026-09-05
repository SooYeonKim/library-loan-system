package com.library.loan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class LoanConfig {

    @Bean
    public LoanPolicy loanPolicy(Clock clock) {
        return new LoanPolicy(clock);
    }
}
