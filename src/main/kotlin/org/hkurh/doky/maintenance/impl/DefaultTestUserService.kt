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

package org.hkurh.doky.maintenance.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.maintenance.TestUserService
import org.hkurh.doky.users.db.UserEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultTestUserService(
    @Value("\${doky.email.test.prefix}") private val testEmailPrefix: String,
    private val documentEntityRepository: DocumentEntityRepository,
    private val userEntityRepository: UserEntityRepository
) : TestUserService {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun cleanupTestUsers() {
        require(testEmailPrefix.trim().isNotBlank()) {
            "Refusing to cleanup test users: doky.email.test.prefix is blank (would match everything)."
        }

        log.info { "Cleaning up test users with prefix=['$testEmailPrefix']" }

        val deletedDocumentsCount = documentEntityRepository.deleteAllByUserPrefix(testEmailPrefix)
        log.debug { "Deleted [$deletedDocumentsCount] test user documents with email prefix ['$testEmailPrefix']" }

        val deletedUsersCount = userEntityRepository.deleteByUidPrefix(testEmailPrefix)
        log.debug { "Deleted [$deletedUsersCount] test user accounts with email prefix ['$testEmailPrefix']" }
    }
}
