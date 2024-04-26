package org.hkurh.doky.search.api

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "Documents")
@SecurityRequirement(name = "Bearer Token")
interface DocumentSearchApi {

    @GetMapping
    fun search(q: String? = "*"): ResponseEntity<*>
}
