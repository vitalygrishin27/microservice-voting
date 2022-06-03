package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PerformanceException extends VotingException {
    public PerformanceException(String message) {
        super(message);
    }

    public PerformanceException(String message, HttpStatus status) {
        super(message, status);
    }
}
