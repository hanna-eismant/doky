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

package org.hkurh.doky.documents.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.StringUtils.isBlank
import org.hkurh.doky.documents.DocumentFacade
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.DownloadTokenService
import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.hkurh.doky.toDto
import org.hkurh.doky.users.UserService
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Component
class DefaultDocumentFacade(
    private val documentService: DocumentService,
    private val downloadTokenService: DownloadTokenService,
    private val fileStorageService: FileStorageService,
    private val userService: UserService
) : DocumentFacade {
    override fun createDocument(name: String, description: String?): DocumentResponse? {
        val documentEntity = documentService.create(name, description)
        LOG.debug { "Created new Document with id [${documentEntity.id}]" }
        return documentEntity.toDto()
    }

    override fun update(id: Long, document: DocumentRequest) {
        val existedDocument =
            documentService.find(id) ?: throw DokyNotFoundException("Document with id [$id] not found")
        existedDocument.apply {
            name = document.name
            description = document.description
            documentService.save(this)
        }
    }

    override fun findDocument(id: Long): DocumentResponse? {
        return documentService.find(id)?.toDto()
    }

    override fun findAllDocuments(): List<DocumentResponse?> {
        return documentService.find().map { it.toDto() }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun saveFile(id: Long, file: MultipartFile) {
        val document = documentService.find(id) ?: throw DokyNotFoundException("Document with id [$id] not found")
        try {
            val path = fileStorageService.store(file, document.filePath)
            document.filePath = path
            document.fileName = file.originalFilename
            documentService.save(document)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    override fun getFile(documentId: Long, token: String): Resource {
        val document = downloadTokenService.validateDownloadTokenAndFetchDocument(documentId, token)
        val filePath = document.filePath

        if (isBlank(filePath)) throw DokyNotFoundException("No attached file for Document [$documentId]")
        val file = fileStorageService.getFile(filePath!!)
            ?: throw DokyNotFoundException("File [$filePath] attached to document [$documentId] does not exists in storage")

        LOG.debug { "Download file for Document [$documentId] with URI [${file.toUri()}]" }
        return UrlResource(file.toUri())
    }

    override fun generateDownloadToken(id: Long): String {
        val user = userService.getCurrentUser()
        val document = documentService.find(id) ?: throw DokyNotFoundException("Document with id [$id] not found")
        LOG.debug { "Generate token for user [${user.id}] and document [$id]" }
        val token = downloadTokenService.generateDownloadToken(user, document)
        return token
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
