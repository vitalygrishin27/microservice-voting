package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CriteriaException extends VotingException {
    public CriteriaException(String message) {
        super(message);
    }

    public CriteriaException(String message, HttpStatus status) {
        super(message, status);
    }
}
