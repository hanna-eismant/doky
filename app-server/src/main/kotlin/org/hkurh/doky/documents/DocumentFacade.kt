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
    fun update(id: Long, document: DocumentRequest)

    /**
     * Finds a document with the specified ID.
     *
     * @param id The ID of the document to find.
     * @return The [DocumentResponse] object representing the found document, or null if the document is not found.
     */
    fun findDocument(id: Long): DocumentResponse?

    /**
     * Retrieves a list of all documents.
     *
     * @return A list of [DocumentResponse] objects representing the retrieved documents. The list may contain null values if the document retrieval failed.
     */
    fun findAllDocuments(): List<DocumentResponse?>

    /**
     * Saves a file and attach it to document with the given ID.
     *
     * @param file The file to be saved.
     * @param id The ID of the document to which the file should be attached.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    fun saveFile(id: Long, file: MultipartFile)

    /**
     * Retrieves the file associated with the specified document ID and authorized by the given token.
     *
     * @param documentId The ID of the document whose file is being retrieved.
     * @param token The authorization token used to verify the download request.
     * @return A [Resource] representing the file associated with the specified document ID.
     * @throws IOException If an error occurs while accessing the file.
     */
    @Throws(IOException::class)
    fun getFile(documentId: Long, token: String): Resource

    /**
     * Generates a download token for the specified document ID and current user.
     *
     * @param id The ID of the document for which the download token is being generated.
     * @return A string representing the generated download token.
     */
    fun generateDownloadToken(id: Long): String
}
