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

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*


@Entity
@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "documents")
class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    var createdDate: Date? = null

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: UserEntity? = null

    @Column(name = "modified_date")
    @LastModifiedDate
    var modifiedDate: Date? = null

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "modified_by")
    var modifiedBy: UserEntity? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Lob
    @Column(name = "description")
    var description: String? = null

    @Column(name = "file_path")
    var filePath: String? = null

    @Column(name = "file_name")
    var fileName: String? = null

    @ManyToOne
    @JoinColumn(name = "creator_id")
    var creator: UserEntity? = null
}
