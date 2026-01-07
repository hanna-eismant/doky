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

package org.hkurh.doky.password.db

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
import java.util.*

@Entity
@Table(
    name = "reset_password_tokens",
    indexes = [Index(name = "idx_reset_password_tokens_token", columnList = "token")],
    uniqueConstraints = [UniqueConstraint(name = "uc_reset_password_tokens_token", columnNames = ["token"])]
)
class ResetPasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @OneToOne
    @JoinColumn(name = "app_user", nullable = false)
    lateinit var user: UserEntity

    @Column(name = "token", nullable = false, unique = true)
    lateinit var token: String

    @Column(name = "expiration_date", nullable = false)
    lateinit var expirationDate: Date

    @Column(name = "sent_email", nullable = false)
    var sentEmail: Boolean = false
}
