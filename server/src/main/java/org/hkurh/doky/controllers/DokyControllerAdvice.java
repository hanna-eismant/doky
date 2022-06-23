package org.hkurh.doky.controllers;

import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DokyControllerAdvice {

    @ExceptionHandler(value = {
            DokyAuthenticationException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
