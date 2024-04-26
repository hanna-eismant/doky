package org.hkurh.doky.documents

import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.api.DocumentResponse
import org.springframework.core.io.Resource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * Represents a facade for managing documents.
 */
interface DocumentFacade {
    /**
     * Creates a document with the specified name and description.
     *
     * @param name The name of the document.
     * @param description The description of the document.
     * @return A [DocumentResponse] object representing the created document, or null if the document creation failed.
     */
    fun createDocument(name: String, description: String?): DocumentResponse?

    /**
     * Updates a document with the specified ID using the provided document request.
     *
     * @param id The ID of the document to update.
     * @param document The document request containing the updated information.
     */
    fun update(id: String, document: DocumentRequest)

    /**
     * Finds a document with the specified ID.
     *
     * @param id The ID of the document to find.
     * @return The [DocumentResponse] object representing the found document, or null if the document is not found.
     */
    fun findDocument(id: String): DocumentResponse?

    /**
     * Saves a file and attach it to document with the given ID.
     *
     * @param file The file to be saved.
     * @param id The ID of the document to which the file should be attached.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    fun saveFile(file: MultipartFile, id: String)

    /**
     * Retrieves the resource representing the file attached to the document with the specified ID.
     *
     * @param id The ID of the document.
     * @return The [Resource] object representing the attached file, or null if the file is not found.
     * @throws IOException If an I/O error occurs while retrieving the file.
     */
    @Throws(IOException::class)
    fun getFile(id: String): Resource?
}
