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

package org.hkurh.doky.documents.api

/**
 * Represents a document response.
 *
 * @property id The ID of the document.
 * @property name The name of the document.
 * @property description The description of the document.
 * @property fileName The name of the file attached to the document.
 * @property createdBy The username of the user who created the document.
 * @property createdDate The date when the document was created.
 * @property modifiedBy The username of the user who last modified the document.
 * @property modifiedDate The date of the last modification of the document.
 */
class DocumentResponse {
    var id: Long = 0
    var name: String? = null
    var description: String? = null
    var fileName: String? = null
    lateinit var createdBy: String
    lateinit var createdDate: String
    lateinit var modifiedBy: String
    lateinit var modifiedDate: String
}
