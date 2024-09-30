package org.hkurh.doky

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api")
class DokyController : DokyApi {

    @GetMapping("/app-version")
    override fun appVersion(): VersionResponse {
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
