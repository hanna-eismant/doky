package org.hkurh.doky

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DokyController {

    @Value("\${doky.version.build}")
    lateinit var build: String

    @GetMapping("/")
    fun root() : ResponseEntity<*> {
        return ResponseEntity.ok().body("OK")
    }

    @GetMapping("/version")
    fun version(): VersionResponse {
        return VersionResponse(build)
    }
}

data class VersionResponse(val message: String = "") {
    val schemaVersion = 1
    val label = "version"
    val color = "green"
}
