package org.hkurh.doky.search.api

import org.hkurh.doky.search.DocumentSearchFacade
import org.hkurh.doky.security.DokyAuthority
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/documents")
@Secured(DokyAuthority.Role.ROLE_USER)
class DocumentSearchController(
    private val documentSearchFacade: DocumentSearchFacade
) : DocumentSearchApi {

    @GetMapping("/search")
    fun search(@RequestParam(name = "q") query: String): ResponseEntity<*> {
        val documents = documentSearchFacade.search(query)
        return ResponseEntity.ok(documents)
    }
}
