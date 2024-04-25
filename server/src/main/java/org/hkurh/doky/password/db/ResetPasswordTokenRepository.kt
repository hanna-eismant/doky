package org.hkurh.doky.password.db

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository


interface ResetPasswordTokenRepository :
    CrudRepository<ResetPasswordTokenEntity, Long>, JpaSpecificationExecutor<ResetPasswordTokenEntity> {

        fun findByUser(user: UserEntity): ResetPasswordTokenEntity?
    }
