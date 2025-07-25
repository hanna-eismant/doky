/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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

    @ExceptionHandler(DokyInvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun badRequest(exception: Exception): ErrorResponse {
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
