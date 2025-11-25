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

package org.hkurh.doky.search.impl

import com.azure.search.documents.SearchClient
import com.azure.search.documents.models.SearchOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.api.Page
import org.hkurh.doky.documents.api.Sort
import org.hkurh.doky.documents.api.SortDirection
import org.hkurh.doky.search.DocumentResultData
import org.hkurh.doky.search.DocumentSearchService
import org.hkurh.doky.search.SearchResult
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Service

@Service
class DefaultDocumentSearchService(
    private val userService: UserService,
    private val searchClient: SearchClient
) : DocumentSearchService {

    private val log = KotlinLogging.logger {}

    override fun search(query: String, page: Page, sort: Sort): SearchResult {
        val options = SearchOptions()
            .setIncludeTotalCount(true)
            .setFilter(buildAllowedUsersFilter())
            .setSkip(calculateSkip(page))
            .setTop(page.size!!)
            .setOrderBy(generateOrderBy(sort))

        val results = searchClient.search(query, options, null)
        val totalCount = results.totalCount ?: 0L
        val documents = results.mapNotNull { result -> result.getDocument(DocumentResultData::class.java) }
        log.debug { "Search query [$query] returned [$totalCount] results" }
        return SearchResult(documents = documents, totalCount = totalCount)
    }

    fun buildAllowedUsersFilter(): String {
        val allowedUserIds = listOf(userService.getCurrentUser().id)
        val conditions = allowedUserIds.joinToString(" or ") { "u eq '$it'" }
        return "allowedUsers/any(u: $conditions)"
    }

    private fun calculateSkip(page: Page): Int {
        return page.number!! * page.size!!
    }

    private fun generateOrderBy(sort: Sort): String {
        val property = sort.property ?: "search.score()"
        val direction = sort.direction?.name?.lowercase() ?: SortDirection.DESC.name.lowercase()

        return "$property $direction"
    }
}
