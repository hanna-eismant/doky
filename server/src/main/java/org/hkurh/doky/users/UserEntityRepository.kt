package org.hkurh.doky.users

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserEntityRepository : CrudRepository<UserEntity?, Long?>, JpaSpecificationExecutor<UserEntity?> {
    @Query("select u from UserEntity u where u.uid = ?1")
    fun findByUid(uid: String): UserEntity?
}
