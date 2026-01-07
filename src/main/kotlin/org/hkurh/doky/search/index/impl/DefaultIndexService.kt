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

import com.azure.search.documents.SearchClient
import com.azure.search.documents.models.SearchOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.DocumentAccessService
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentResultData
import org.hkurh.doky.search.index.IndexService
import org.hkurh.doky.toIndexData
import org.springframework.stereotype.Service


@Service
class DefaultIndexService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val documentAccessService: DocumentAccessService,
    private val searchClient: SearchClient
) : IndexService {

    private val log = KotlinLogging.logger {}

    override fun fullIndex() {
        cleanIndex()
        val documents = documentEntityRepository.findAll()
            .mapNotNull { documentEntity -> documentEntity?.toIndexData() }
            .map { documentAccessService.populateAllowedUsers(it) }
        val indexingResult = searchClient.uploadDocuments(documents)

        log.debug { "Upload [${indexingResult.results.size}] documents to index" }
        indexingResult.results
            .filter { !it.isSucceeded }
            .forEach { log.error { "Document [${it.key}] upload failed: [${it.errorMessage}]" } }
    }

    private fun cleanIndex() {
        val pageSize = 1_000
        var totalDeleted = 0
        val options = SearchOptions().setTop(pageSize)

        while (true) {
            val page = searchClient.search("*", options, null)
            val deleteBatch = page.mapNotNull { result -> result.getDocument(DocumentResultData::class.java) }
                .map { it.id.trim() }
                .filter { it.isNotEmpty() }
                .map { DocumentResultData(id = it) }

            if (deleteBatch.isEmpty()) {
                break
            }

            searchClient.deleteDocuments(deleteBatch)
            totalDeleted += deleteBatch.size
            log.debug { "Deleted [${deleteBatch.size}] documents from index. Total deleted: [${totalDeleted}]" }
        }
    }
}
