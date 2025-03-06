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
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.users.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.security.DokyUserDetails
import org.hkurh.doky.security.UserAuthority
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.AuthorityEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DefaultUserService(
    private val userEntityRepository: UserEntityRepository,
    private val authorityEntityRepository: AuthorityEntityRepository,
    private val kafkaEmailNotificationProducerService: KafkaEmailNotificationProducerService,
) : UserService {
    override fun findUserByUid(userUid: String): UserEntity? {
        return userEntityRepository.findByUid(userUid) ?: throw DokyNotFoundException("User doesn't exist")
    }

    override fun create(userUid: String, encodedPassword: String): UserEntity {
        val userEntity = UserEntity().apply {
            uid = userUid
            password = encodedPassword
            name = extractNameFromUid(userUid)
            authorityEntityRepository.findByAuthority(UserAuthority.ROLE_USER)
                ?.let { authorities.add(it) }
        }
        val createdUser = userEntityRepository.save(userEntity)
        LOG.debug { "Created new user [${createdUser.id}]" }
        kafkaEmailNotificationProducerService.sendNotification(createdUser.id, EmailType.REGISTRATION)
        return createdUser
    }

    override fun getCurrentUser(): UserEntity {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        return userEntity!!
    }

    override fun updateUser(user: UserEntity) {
        userEntityRepository.save(user)
        LOG.debug { "User is updated [${user.id}]" }
    }

    private fun extractNameFromUid(userUid: String): String {
        return userUid.split("@").first()
    }

    override fun exists(email: String): Boolean {
        return userEntityRepository.existsByUid(email)
    }

    override fun exists(id: Long): Boolean {
        return userEntityRepository.existsById(id)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
