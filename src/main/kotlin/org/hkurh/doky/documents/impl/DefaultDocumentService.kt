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
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Service

@Service
class DefaultDocumentService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val userService: UserService,
) : DocumentService {

    override fun create(name: String, description: String?): DocumentEntity {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = userService.getCurrentUser()
        document.creator = currentUser
        val savedDocument = documentEntityRepository.save(document)
        LOG.debug { "Created new Document [${savedDocument.id}] by User [${currentUser.id}]" }
        return savedDocument
    }

    override fun find(id: Long): DocumentEntity? {
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByIdAndCreatorId(id, currentUser.id)
    }

    override fun find(): List<DocumentEntity> {
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByCreatorId(currentUser.id)
    }

    override fun save(document: DocumentEntity) {
        documentEntityRepository.save(document)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
