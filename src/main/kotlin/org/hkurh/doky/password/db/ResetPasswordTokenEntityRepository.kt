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

package org.hkurh.doky.password.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*


/**
 * Repository interface for managing reset password tokens.
 * This interface extends the CrudRepository and JpaSpecificationExecutor interfaces.
 */
interface ResetPasswordTokenEntityRepository :
    CrudRepository<ResetPasswordTokenEntity, Long>, JpaSpecificationExecutor<ResetPasswordTokenEntity> {

    fun findByUser(user: UserEntity): ResetPasswordTokenEntity?

    fun findByToken(token: String): ResetPasswordTokenEntity?

    fun findByExpirationDateLessThan(expirationDate: Date): List<ResetPasswordTokenEntity>

    fun deleteByToken(token: String)

    @Query(
        """
        SELECT r FROM ResetPasswordTokenEntity r 
        WHERE r.user.id = :userId AND r.expirationDate > CURRENT_TIMESTAMP AND r.sentEmail = false
    """
    )
    fun findValidUnsentTokensByUserId(userId: Long): List<ResetPasswordTokenEntity>
}
