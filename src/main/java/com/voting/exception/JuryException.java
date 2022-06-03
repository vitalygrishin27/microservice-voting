package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JuryException extends VotingException {
    public JuryException(String message) {
        super(message);
    }

    public JuryException(String message, HttpStatus status) {
        super(message, status);
    }
}
