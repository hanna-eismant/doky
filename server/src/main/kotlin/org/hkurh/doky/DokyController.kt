package org.hkurh.doky

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DokyController {

    @GetMapping("/")
    fun root(): ResponseEntity<*> {
        return ResponseEntity.ok().body("OK")
    }

    @GetMapping("/version")
    fun version(): VersionResponse {
        return VersionResponse(BuildConfig.buildVersion)
    }
}

data class VersionResponse(val message: String = "") {
    @Suppress("Unused")
    val schemaVersion = 1

    @Suppress("Unused")
    val label = "version"

    @Suppress("Unused")
    val color = "green"
}
