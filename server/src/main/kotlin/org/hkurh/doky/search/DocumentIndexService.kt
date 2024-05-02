package org.hkurh.doky.search

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.search.solr.DocumentResultBean
import java.util.*

/**
 * The [DocumentIndexService] interface provides methods for managing the indexing and searching of documents.
 */
interface DocumentIndexService {
    /**
     * This method performs a full index of documents.
     */
    fun fullIndex()

    /**
     * Updates the index of documents.
     *
     * @param runDate The date to use as the reference for updating the index.
     */
    fun updateIndex(runDate: Date)

    /**
     * Performs a search using the given query.
     *
     * @param query The search query.
     * @return A list of [DocumentResultBean] objects representing the search results.
     */
    fun search(query: String): List<DocumentResultBean>


    /**
     * Updates the information of a document in index.
     *
     * @param document The [DocumentEntity] object representing the document to update.
     */
    fun updateDocumentInfo(document: DocumentEntity)
}
