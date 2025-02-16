package org.hkurh.doky.admin

import org.hkurh.doky.search.index.IndexService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Executors

@RestController
@RequestMapping("/api/admin/search")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class SearchController(
    private val indexService: IndexService
) {

    @PostMapping("/index/full")
    fun startFullIndex(): ResponseEntity<*> {
        Executors.newCachedThreadPool().submit { indexService.fullIndex() }
        return ResponseEntity.accepted().build<Any>()
    }
}
