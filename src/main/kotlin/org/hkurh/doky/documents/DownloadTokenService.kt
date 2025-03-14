/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
