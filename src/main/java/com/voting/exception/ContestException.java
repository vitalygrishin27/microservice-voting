package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ContestException extends VotingException {
    public ContestException(String message) {
        super(message);
    }

    public ContestException(String message, HttpStatus status) {
        super(message, status);
    }
}
