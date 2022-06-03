package com.voting.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Controller
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {VotingException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ((VotingException) ex).getErrorMessage(),
                new HttpHeaders(), ((VotingException) ex).getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleRootConflict(RuntimeException ex, WebRequest request) {
        String userMessage= ex.getMessage();
        if(userMessage.contains("ConstraintViolationException")){
            userMessage = "Entities constraint violation";
        }
        VotingException runtimeException = new VotingException(userMessage);
        return handleExceptionInternal(ex, runtimeException.getErrorMessage(),
                new HttpHeaders(), runtimeException.getStatus(), request);
    }
}
