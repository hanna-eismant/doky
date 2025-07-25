/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
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

package org.hkurh.doky.documents.impl

import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.DownloadTokenService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntity
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntityRepository
import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.security.impl.DefaultJwtProvider
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultDownloadTokenService(
    private val documentService: DocumentService,
    private val userService: UserService,
    private val downloadDocumentTokenEntityRepository: DownloadDocumentTokenEntityRepository,
    private val jwtProvider: DefaultJwtProvider
) : DownloadTokenService {

    override fun generateDownloadToken(user: UserEntity, document: DocumentEntity): String {
        val token = jwtProvider.generateDownloadToken(user)
        val tokenEntity =
            downloadDocumentTokenEntityRepository.findByUserAndDocument(user, document) ?: DownloadDocumentTokenEntity()
        tokenEntity.apply {
            this.user = user
            this.document = document
            this.token = token
        }
        downloadDocumentTokenEntityRepository.save(tokenEntity)
        return token
    }

    override fun validateDownloadTokenAndFetchDocument(documentId: Long, token: String): DocumentEntity {
        try {
            if (!jwtProvider.isDownloadTokenValid(token)) throw DokyInvalidTokenException("Token is expired or invalid")
        } catch (e: Exception) {
            throw DokyInvalidTokenException("Error occurred during token validation: ${e.message}")
        }

        val user = userService.getCurrentUser()
        val document =
            documentService.find(documentId)
                ?: throw DokyInvalidTokenException("Document with id [$documentId] not found")
        val documentToken = downloadDocumentTokenEntityRepository.findByUserAndDocument(user, document)
        if (documentToken == null || documentToken.token != token) {
            throw DokyInvalidTokenException("Token [$token] is not valid for document [$documentId] and user [${user.id}]")
        }
        return document
    }
}
