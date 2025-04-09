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

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hkurh.doky.users.db.UserEntity

@Entity
@Table(
    name = "download_document_tokens",
    indexes = [Index(name = "idx_download_document_tokens_token", columnList = "token")],
    uniqueConstraints = [
        UniqueConstraint(name = "uc_download_document_tokens_token", columnNames = ["token"]),
        UniqueConstraint(
            name = "uc_download_document_tokens_app_user_document",
            columnNames = ["app_user", "document"]
        )]
)
class DownloadDocumentTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null

    @OneToOne
    @JoinColumn(name = "app_user", nullable = false, unique = true)
    lateinit var user: UserEntity

    @OneToOne
    @JoinColumn(name = "document", nullable = false, unique = true)
    lateinit var document: DocumentEntity

    @Column(name = "token", nullable = false, unique = true)
    lateinit var token: String
}
