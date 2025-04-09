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
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity

/**
 * Represents a service for managing documents.
 */
interface DocumentService {

    /**
     * Creates a document with the specified name and description.
     *
     * @param name The name of the document.
     * @param description The description of the document.
     * @return A [DocumentEntity] object representing the created document.
     */
    fun create(name: String, description: String?): DocumentEntity

    /**
     * Finds a document by its ID.
     *
     * @param id The ID of the document to find.
     * @return The [DocumentEntity] object representing the found document, or null if no document found.
     */
    fun find(id: Long): DocumentEntity?

    /**
     * Finds all documents.
     *
     * @return A list of [DocumentEntity] objects representing the found documents.
     */
    fun find(): List<DocumentEntity>

    /**
     * Saves the given document.
     *
     * @param document The [DocumentEntity] object to be saved.
     */
    fun save(document: DocumentEntity)
}
