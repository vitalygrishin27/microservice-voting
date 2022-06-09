package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MarkException extends VotingException {
    public MarkException(String message) {
        super(message);
    }

    public MarkException(String message, HttpStatus status) {
        super(message, status);
    }
}
