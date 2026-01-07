/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.documents

import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.api.DocumentSearchResponse
import org.hkurh.doky.documents.api.Page
import org.hkurh.doky.documents.api.Sort
import org.springframework.core.io.Resource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

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
    fun findAllDocuments(): List<DocumentResponse>

    /**
     * Performs a search for documents based on the provided query, pagination, and sorting options.
     *
     * @param query The search query string used to filter documents.
     * @param page An object containing pagination details such as page number and size.
     * @param sort An object specifying the sorting criteria, including property and direction.
     * @return A [DocumentSearchResponse] object containing the list of documents and the total count of documents found.
     */
    fun search(query: String, page: Page, sort: Sort): DocumentSearchResponse

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
     */
    fun getFile(documentId: Long, token: String): Resource

    /**
     * Generates a download token for the specified document ID and current user.
     *
     * @param id The ID of the document for which the download token is being generated.
     * @return A string representing the generated download token.
     */
    fun generateDownloadToken(id: Long): String
}
