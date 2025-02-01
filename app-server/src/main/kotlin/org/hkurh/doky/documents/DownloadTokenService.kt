package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.users.db.UserEntity

interface DownloadTokenService {

    /**
     * Generates a download token for a specified document and user.
     *
     * @param user The [UserEntity] representing the user requesting the download token.
     * @param document The [DocumentEntity] representing the document for which the token is generated.
     * @return A string representing the generated download token.
     */
    fun generateDownloadToken(user: UserEntity, document: DocumentEntity): String

    /**
     * Validates a download token for the specified document ID.
     *
     * @param documentId The ID of the document to validate the token against.
     * @param token The download token to be validated.
     * @return The [DocumentEntity] associated with the validated token.
     * @throws IllegalArgumentException If the token is invalid or does not match the document.
     */
    fun validateDownloadTokenAndFetchDocument(documentId: Long, token: String): DocumentEntity
}
