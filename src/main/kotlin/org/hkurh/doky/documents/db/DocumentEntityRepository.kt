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

package org.hkurh.doky.documents.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * Repository interface for managing DocumentEntity objects.
 * Inherits from CrudRepository and JpaSpecificationExecutor.
 */
interface DocumentEntityRepository : CrudRepository<DocumentEntity, Long>, JpaSpecificationExecutor<DocumentEntity> {

    @EntityGraph(attributePaths = ["createdBy", "modifiedBy", "creator"])
    @Query("select d from DocumentEntity d where d.creator = :creator")
    fun findByCreatorId(creator: UserEntity): List<DocumentEntity>

    @EntityGraph(attributePaths = ["createdBy", "modifiedBy", "creator"])
    @Query("select d from DocumentEntity d where d.id = :documentId and d.creator= :creator")
    fun findByIdAndCreatorId(documentId: Long, creator: UserEntity): DocumentEntity?

    @Query("select d.creator.id from DocumentEntity d where d.id = :documentId")
    fun findAllowedUsers(@Param("documentId") documentId: Long): List<Long>

    @Query("select d from DocumentEntity d where d.id in :ids and d.creator = :creator")
    fun findAllById(ids: List<Long>, creator: UserEntity): List<DocumentEntity>

    @Modifying
    @Query("delete from DocumentEntity d where d.creator.uid like :uidPrefix% or d.createdBy.uid like :uidPrefix% or d.modifiedBy.uid like :uidPrefix%")
    fun deleteAllByUserPrefix(@Param("uidPrefix") uidPrefix: String) : Int
}
