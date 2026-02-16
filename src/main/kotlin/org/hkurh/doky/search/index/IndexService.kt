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

package org.hkurh.doky.search.index

/**
 * Interface defining the contract for indexing operations.
 *
 * IndexService provides a method for handling full indexing of documents,
 * which may include cleaning existing indexes and re-populating them
 * with data from associated repositories.
 */
interface IndexService {

    /**
     * Executes the process of full indexing for the system.
     *
     * This method is responsible for completely rebuilding the index structure.
     * It may involve cleaning existing indexes and re-populating them with
     * data from their respective data sources or repositories. Typically used
     * in scenarios requiring a fresh or updated index without relying on
     * incremental updates.
     */
    fun fullIndex()

    /**
     * Updates the index for a specific document.
     *
     * This method is responsible for partially updating the search index
     * for the given document, identified by its unique identifier. It is
     * typically used to apply incremental changes to the index when a
     * document is modified or added within the system.
     *
     * @param documentId The unique identifier of the document to update in the index.
     */
    fun updateIndex(documentId: Long)
}
