package com.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends VotingException {
    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, HttpStatus status) {
        super(message, status);
    }
}
