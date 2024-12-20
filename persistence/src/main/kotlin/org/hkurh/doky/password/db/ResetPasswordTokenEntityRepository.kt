package org.hkurh.doky.password.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
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
}
