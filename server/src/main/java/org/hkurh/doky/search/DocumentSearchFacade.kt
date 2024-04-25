package org.hkurh.doky.search

import org.hkurh.doky.documents.api.DocumentResponse

interface DocumentSearchFacade {
    fun search(query: String): List<DocumentResponse>
}
