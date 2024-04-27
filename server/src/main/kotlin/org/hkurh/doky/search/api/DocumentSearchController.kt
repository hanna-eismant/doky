package org.hkurh.doky.search.api

import org.hkurh.doky.search.DocumentSearchFacade
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Represents a controller for searching documents.
 *
 * This controller handles searching documents based on a query parameter.
 */
@RestController
@RequestMapping("/documents")
@PreAuthorize("hasRole('ROLE_USER')")
class DocumentSearchController(
    private val documentSearchFacade: DocumentSearchFacade
) : DocumentSearchApi {

    @GetMapping
    override fun search(@RequestParam q: String?): ResponseEntity<*> {
        val query = q?.ifEmpty { "*" } ?: "*"
        val documents = documentSearchFacade.search(query)
        return ResponseEntity.ok(documents)
    }
}
