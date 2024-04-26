package org.hkurh.doky.password.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository


/**
 * Repository interface for managing reset password tokens.
 * This interface extends the CrudRepository and JpaSpecificationExecutor interfaces.
 */
interface ResetPasswordTokenRepository :
    CrudRepository<ResetPasswordTokenEntity, Long>, JpaSpecificationExecutor<ResetPasswordTokenEntity> {

        fun findByUser(user: UserEntity): ResetPasswordTokenEntity?
    }
