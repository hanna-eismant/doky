package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.users.db.UserEntity

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
     * Finds all documents.
     *
     * @return A list of [DocumentEntity] objects representing the found documents.
     */
    fun find(): List<DocumentEntity>

    /**
     * Saves the given document.
     *
     * @param document The [DocumentEntity] object to be saved.
     */
    fun save(document: DocumentEntity)

    /**
     * Generates a download token for a specified document and user.
     *
     * @param user The [UserEntity] representing the user requesting the download token.
     * @param document The [DocumentEntity] representing the document for which the token is generated.
     * @return A string representing the generated download token.
     */
    fun generateDownloadToken(user: UserEntity, document: DocumentEntity): String
}
