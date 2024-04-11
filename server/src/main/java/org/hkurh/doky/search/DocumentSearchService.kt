package org.hkurh.doky.search

import org.springframework.stereotype.Service

@Service
class DocumentSearchService(
    private val documentIndexService: DocumentIndexService
) {

//    fun search(id: Long, queryWords: List<String>): List<DocumentBean> {
//        return documentIndexService.search()
//    }
}
