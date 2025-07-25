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

package org.hkurh.doky.search.index.impl

import com.azure.search.documents.SearchClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentIndexData
import org.hkurh.doky.search.index.IndexService
import org.hkurh.doky.toIndexData
import org.springframework.stereotype.Service

@Service
class DefaultIndexService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val searchClient: SearchClient
) : IndexService {

    private val log = KotlinLogging.logger {}

    override fun fullIndex() {
        cleanIndex()
        val documents = documentEntityRepository.findAll()
            .mapNotNull { documentEntity -> documentEntity?.toIndexData() }
        val indexingResult = searchClient.uploadDocuments(documents)
        log.debug { "Upload [${indexingResult.results.size}] documents to index" }
        indexingResult.results.forEach {
            if (!it.isSucceeded) {
                log.error { "Document [${it.key}] upload failed: [${it.errorMessage}]" }
            }
        }
    }

    private fun cleanIndex() {
        val results = searchClient.search("*")
            .map { result -> result.getDocument(DocumentIndexData::class.java) }
        if (results.isNotEmpty()) {
            log.debug { "Deleting [${results.size}] documents from index" }
            searchClient.deleteDocuments(results)
        }
    }
}
