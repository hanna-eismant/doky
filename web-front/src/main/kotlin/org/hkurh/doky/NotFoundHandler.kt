package org.hkurh.doky

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.IOException
import java.nio.charset.Charset


@ControllerAdvice
class NotFoundHandler {

    @Value("\${doky.spa.default-file}")
    lateinit var defaultFile: String

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @ExceptionHandler(NoHandlerFoundException::class)
    fun renderDefaultPage(): ResponseEntity<String> {
        try {
            val indexFile = resourceLoader.getResource(defaultFile).inputStream
            val body = StreamUtils.copyToString(indexFile, Charset.defaultCharset())
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body)
        } catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("There was an error completing the action.")
        }
    }
}
