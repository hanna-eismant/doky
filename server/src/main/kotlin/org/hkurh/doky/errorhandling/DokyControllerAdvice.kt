package org.hkurh.doky.errorhandling

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.util.ResourceUtils
import org.springframework.util.StreamUtils
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset


@RestControllerAdvice
class DokyControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException::class)
    fun renderDefaultPage(exception: NoHandlerFoundException): ResponseEntity<String> {
        val defaultResource = "/index.html"

        try {
            var resourceFile = exception.requestURL
            var resourceType = ""

            when {
                resourceFile == "/main.css" -> {
                    resourceType = "text/css"
                }

                resourceFile == "/bundle.js" -> {
                    resourceType = "application/javascript"
                }

                Regex(".svg$").containsMatchIn(resourceFile) -> {
                    resourceType = "image/svg+xml"
                }

                Regex(".woff$").containsMatchIn(resourceFile) -> {
                    resourceType = "application/font-woff"
                }

                Regex(".woff2$").containsMatchIn(resourceFile) -> {
                    resourceType = "font/woff2"
                }

                else -> {
                    resourceType = "text/html"
                    resourceFile = defaultResource
                }
            }

            val file: File = ResourceUtils.getFile("classpath:static$resourceFile")
            val inputStream = FileInputStream(file)
            val body: String = StreamUtils.copyToString(inputStream, Charset.defaultCharset())
            val parseMediaType = MediaType.parseMediaType(resourceType)
            val response = ResponseEntity.ok()
            parseMediaType.let { response.contentType(it) }
            return response.body(body)
        } catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There was an error completing the action.")
        }
    }

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
