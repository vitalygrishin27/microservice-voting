package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CategoryException extends VotingException {
    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, HttpStatus status) {
        super(message, status);
    }
}
