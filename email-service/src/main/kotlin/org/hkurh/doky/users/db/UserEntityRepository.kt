package org.hkurh.doky.users.db

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

/**
 * The [UserEntityRepository] interface extends the [CrudRepository] and [JpaSpecificationExecutor] interfaces to provide
 * CRUD operations and advanced queries for [UserEntity] objects.
 */
interface UserEntityRepository : CrudRepository<UserEntity?, Long?>, JpaSpecificationExecutor<UserEntity?> {

    fun findByUid(uid: String): UserEntity?

    fun existsByUid(uid: String): Boolean
}
