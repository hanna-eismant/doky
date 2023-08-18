package org.hkurh.doky.errorhandling

import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException

@RestControllerAdvice
class DokyControllerAdvice {
    @ExceptionHandler(value = [DokyAuthenticationException::class, AccessDeniedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun authenticationException(exception: Exception): ErrorResponse {
        return ErrorResponse(Error(exception.message!!))
    }

    @ExceptionHandler(DokyRegistrationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun registrationException(exception: DokyRegistrationException): ErrorResponse {
        return ErrorResponse(Error(exception.message!!))
    }

    @ExceptionHandler(DokyNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(exception: DokyNotFoundException): ErrorResponse {
        return ErrorResponse(Error(exception.message!!))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun validationException(exception: MethodArgumentNotValidException): ValidationErrorResponse {
        val response = ValidationErrorResponse(Error("Validation failed"))
        exception.bindingResult.fieldErrors
            .groupBy { it.field }
            .forEach { field ->
                response.fields.add(Field(field.key,
                    field.value
                        .map { it.defaultMessage ?: "" }
                        .sortedDescending()
                ))
            }
        return response
    }

    @ExceptionHandler(IOException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun ioException(exception: IOException): ErrorResponse {
        return ErrorResponse(Error(exception.message!!))
    }
}
