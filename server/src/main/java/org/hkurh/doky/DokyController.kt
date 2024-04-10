package org.hkurh.doky

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DokyController {

    @Value("\${doky.version.build}")
    lateinit var build: String

    @GetMapping("/version")
    fun version(): VersionDto {
        return VersionDto(build)
    }
}

data class VersionDto(val message: String = "") {
    val schemaVersion = "1"
    val label = "version"
    val color = "green"
}
