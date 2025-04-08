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

package org.hkurh.doky.documents.db

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Repository interface for managing DocumentEntity objects.
 * Inherits from CrudRepository and JpaSpecificationExecutor.
 */
interface DocumentEntityRepository : CrudRepository<DocumentEntity?, Long?>, JpaSpecificationExecutor<DocumentEntity?> {

    @Query("select d from DocumentEntity d where d.creator.id = ?1")
    fun findByCreatorId(documentId: Long): List<DocumentEntity>

    @Query("select d from DocumentEntity d where d.id = ?1 and d.creator.id = ?2")
    fun findByIdAndCreatorId(documentId: Long, creatorId: Long): DocumentEntity?

    @Query("select d from DocumentEntity d where d.modifiedDate >= ?1")
    fun findLatestModified(modifiedDate: Date): List<DocumentEntity>

    @Query(
        """
            select d from DocumentEntity d 
            where d.id in (:documentIdList) 
            and (d.creator.id = :userId or d.createdBy.id = :userId or d.modifiedBy.id = :userId)
        """
    )
    fun findByListIdAndUserId(documentIdList: List<Long>, userId: Long): List<DocumentEntity>
}
