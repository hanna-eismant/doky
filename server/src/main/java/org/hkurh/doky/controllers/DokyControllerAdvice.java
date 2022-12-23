package org.hkurh.doky.controllers;

import org.hkurh.doky.controllers.data.ErrorResponse;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.exceptions.DokyRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DokyControllerAdvice {

    @ExceptionHandler(value = {
            DokyAuthenticationException.class,
            AccessDeniedException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationException(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = DokyRegistrationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse registrationException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
