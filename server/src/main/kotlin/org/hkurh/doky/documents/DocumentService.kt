package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity

/**
 * Represents a service for managing documents.
 */
interface DocumentService {
    /**
     * Creates a document with the specified name and description.
     *
     * @param name The name of the document.
     * @param description The description of the document.
     * @return A [DocumentEntity] object representing the created document.
     */
    fun create(name: String, description: String?): DocumentEntity

    /**
     * Finds a document by its ID.
     *
     * @param id The ID of the document to find.
     * @return The [DocumentEntity] object representing the found document, or null if no document found.
     */
    fun find(id: String): DocumentEntity?

    /**
     * Saves the given document.
     *
     * @param document The [DocumentEntity] object to be saved.
     */
    fun save(document: DocumentEntity)
}
