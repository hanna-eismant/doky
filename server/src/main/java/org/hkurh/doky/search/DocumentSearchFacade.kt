package org.hkurh.doky.search

import org.hkurh.doky.documents.api.DocumentResponse

/**
 * Represents a facade for searching documents.
 */
interface DocumentSearchFacade {
    /**
     * Performs a search for documents based on the given query.
     *
     * @param query The query string used for searching documents.
     * @return A list of [DocumentResponse] objects representing the search results.
     */
    fun search(query: String): List<DocumentResponse>
}
