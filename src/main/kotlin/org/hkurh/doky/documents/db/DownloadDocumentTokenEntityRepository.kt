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

package org.hkurh.doky.documents.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository


/**
 * Repository interface for managing [DownloadDocumentTokenEntity] objects.
 * Provides methods for querying and persisting [DownloadDocumentTokenEntity] instances in the database.
 * Inherits functionality from [CrudRepository] for common CRUD operations and [JpaSpecificationExecutor]
 * for specification-based queries.
 */
interface DownloadDocumentTokenEntityRepository : CrudRepository<DownloadDocumentTokenEntity?, Long?>,
    JpaSpecificationExecutor<DownloadDocumentTokenEntity?> {

    fun findByUserAndDocument(user: UserEntity, document: DocumentEntity): DownloadDocumentTokenEntity?

}
