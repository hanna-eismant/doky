package org.hkurh.doky.search

import org.springframework.stereotype.Service

@Service
class DocumentSearchService(
    private val documentIndexService: DocumentIndexService
) {

    fun search(): List<DocumentBean> {
        return documentIndexService.search()
    }
}
