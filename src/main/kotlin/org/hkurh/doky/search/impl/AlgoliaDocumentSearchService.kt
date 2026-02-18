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

package org.hkurh.doky.search.impl

import com.algolia.api.SearchClient
import com.algolia.model.search.SearchParamsObject
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.api.Page
import org.hkurh.doky.documents.api.Sort
import org.hkurh.doky.documents.api.SortDirection
import org.hkurh.doky.search.DocumentResultData
import org.hkurh.doky.search.DocumentSearchService
import org.hkurh.doky.search.SearchResult
import org.hkurh.doky.users.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Service implementation for performing document search operations using Algolia.
 *
 * This service uses the Algolia search client to perform indexing and search
 * operations for documents. It works by generating the required query parameters based
 * on the input query, page, sort criteria, and user access restrictions.
 *
 * @constructor Creates an instance of [AlgoliaDocumentSearchService].
 * @param userService The service for retrieving the currently logged-in user and user-related operations.
 * @param searchClient The Algolia search client to perform search operations.
 * @param indexName The base name of the Algolia index as defined in the application configuration.
 *        Can be used as a fallback when no sorting-specific replica index is defined.
 */
@Service
class AlgoliaDocumentSearchService(
    private val userService: UserService,
    private val searchClient: SearchClient,
    @Value("\${algolia.search.index-name}") private val indexName: String = "",
) : DocumentSearchService {

    private val log = KotlinLogging.logger {}

    private val defaultPageSize = 10
    private val minPageSize = 1
    private val maxPageSize = 1_000
    private val minPageNumber = 0

    override fun search(query: String, page: Page, sort: Sort): SearchResult {
        val pageNumber = (page.number ?: minPageNumber).coerceAtLeast(minPageNumber)
        val pageSize = (page.size ?: defaultPageSize).coerceIn(minPageSize, maxPageSize)

        val currentUserUid = userService.getCurrentUser().id
        val filters = buildAllowedUsersFilter(currentUserUid)

        val targetIndex = resolveIndexNameForSort(sort)

        val params = SearchParamsObject()
            .setQuery(query)
            .setPage(pageNumber)          // Algolia page is 0-based
            .setHitsPerPage(pageSize)
            .setFilters(filters)

        val result = searchClient.searchSingleIndex(targetIndex, params, DocumentResultData::class.java)
        log.debug { "Search query=[$query], index=[$targetIndex], page=[$pageNumber], size=[$pageSize], nbHits=[${result.nbHits}]" }
        log.debug { "Search result: [$result]" }
        return SearchResult(result.hits, result.nbHits ?: 0, result.page ?: 0,result.nbPages ?: 0)
    }

    private fun buildAllowedUsersFilter(userId: Long): String {
        return "allowedUsers:\"$userId\""
    }

    /**
     * Algolia sorting is normally done via replica indices.
     * Example replica naming convention:
     *   <baseIndex>_createdDateTs_desc
     *   <baseIndex>_createdDateTs_asc
     *
     * If you don't have replicas for some sorts, fall back to the base index.
     */
    private fun resolveIndexNameForSort(sort: Sort): String {
        val property = sort.property?.trim().takeUnless { it.isNullOrEmpty() } ?: return indexName
        val direction = sort.direction ?: return indexName

        val normalizedProperty = when (property) {
            // request -> index attribute
            "createdDate" -> "createdDateTs"
            "modifiedDate" -> "modifiedDateTs"

            // direct attributes
            "fileName" -> "fileName"
            "name" -> "name"

            else -> return indexName // unknown sort field => safe fallback
        }

        val suffix = when (direction) {
            SortDirection.ASC -> "asc"
            SortDirection.DESC -> "desc"
        }

        return "${indexName}_${normalizedProperty}_$suffix"
    }
}
