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

package org.hkurh.doky.search.index.impl

import com.algolia.api.SearchClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.DocumentAccessService
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.index.IndexService
import org.hkurh.doky.toIndexData
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Service class for managing and synchronizing indexes in Algolia.
 *
 * This implementation focuses on ensuring that document data is accurately indexed
 * and synchronized with the Algolia search engine through operations such as full
 * re-indexing of all documents and updating specific document indexes.
 *
 * @constructor Initializes the service with required dependencies such as repositories,
 * access services, and the search client.
 *
 * @param documentEntityRepository The repository to retrieve document entities from the database.
 * @param documentAccessService Service for managing and populating document-specific access control.
 * @param searchClient The client for interacting with the Algolia search engine.
 * @param indexName The name of the Algolia search index to be managed.
 */
@Service
class AlgoliaIndexService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val documentAccessService: DocumentAccessService,
    private val searchClient: SearchClient,
    @Value("\${algolia.search.index-name}") private val indexName: String = "",
) : IndexService {

    private val log = KotlinLogging.logger {}

    override fun fullIndex() {
        log.info { "Start full index" }

        log.debug { "Clear index data" }
        searchClient.clearObjects(indexName)

        val documents = documentEntityRepository.findAll()
            .mapNotNull { documentEntity -> documentEntity?.toIndexData() }
            .map { documentAccessService.populateAllowedUsers(it) }

        log.debug { "Send index data, size: ${documents.size}" }
        val indexResponse = searchClient.saveObjects(indexName, documents)

        log.debug { "Response: [$indexResponse]" }

        indexResponse
            .map { batchResponse -> batchResponse.taskID }
            .forEach { taskID ->
                log.debug { "Index task id: [$taskID]" }
                searchClient.waitForTask(indexName, taskID)
            }
        log.info { "Finish full index" }
    }

    override fun updateIndex(documentId: Long) {
        log.info { "Start document index [$documentId]" }
        val documentEntity = documentEntityRepository.findByIdOrNull(documentId)
            ?: run {
                log.warn { "Document [$documentId] doesn't exist" }
                return
            }

        val document = documentEntity.toIndexData().also {
            documentAccessService.populateAllowedUsers(it)
        }
        val indexingResponse = searchClient.saveObject(indexName, document)
        log.debug { "Upload document [$documentId] to index" }

        indexingResponse.taskID.let { taskID ->
            log.debug { "Index task id: [$taskID]" }
            searchClient.waitForTask(indexName, taskID)
        }

        log.info { "Finish document index [$documentId]" }
    }
}
