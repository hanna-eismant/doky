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

package org.hkurh.doky.search

import org.hkurh.doky.documents.api.Page
import org.hkurh.doky.documents.api.Sort

/**
 * Provides functionality for searching documents.
 */
fun interface DocumentSearchService {

    /**
     * Searches for documents based on the provided query, pagination, and sorting parameters.
     * Adds filter for allowed users.
     *
     * @param query the search query string used to find matching documents
     * @param page the pagination information, including page number and size
     * @param sort the sorting criteria, including property and direction
     * @return a search result containing a list of document result data and the total count of documents found
     */
    fun search(query: String, page: Page, sort: Sort): SearchResult
}
