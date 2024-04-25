package org.hkurh.doky.users.db

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserEntityRepository : CrudRepository<UserEntity?, Long?>, JpaSpecificationExecutor<UserEntity?> {

    fun findByUid(uid: String): UserEntity?

    fun existsByUid(uid: String): Boolean
}
