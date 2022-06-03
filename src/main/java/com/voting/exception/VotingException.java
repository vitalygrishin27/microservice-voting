package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VotingException extends RuntimeException {
    private String errorMessage;
    private HttpStatus status;

    public VotingException(String message) {
        super(message);
        this.errorMessage = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public VotingException(String message, HttpStatus status) {
        super(message);
        this.errorMessage = message;
        this.status = status;
    }
}
