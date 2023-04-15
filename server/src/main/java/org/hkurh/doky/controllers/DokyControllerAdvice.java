package org.hkurh.doky.controllers;

import org.hkurh.doky.controllers.data.ErrorResponse;
import org.hkurh.doky.controllers.data.ValidationErrorResponse;
import org.hkurh.doky.exceptions.DokyAuthenticationException;
import org.hkurh.doky.exceptions.DokyNotFoundException;
import org.hkurh.doky.exceptions.DokyRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class DokyControllerAdvice {

    @ExceptionHandler(value = {
            DokyAuthenticationException.class,
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(DokyRegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse registrationException(DokyRegistrationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(DokyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(DokyNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse validationException(MethodArgumentNotValidException exception) {
        var bindingResult = exception.getBindingResult();
        var fieldErrors = bindingResult.getFieldErrors();
        var response = new ValidationErrorResponse("Validation failed");

        for (FieldError fieldError : fieldErrors) {
            response.getFields().add(new ValidationErrorResponse.Field(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return response;
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse ioException(IOException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
