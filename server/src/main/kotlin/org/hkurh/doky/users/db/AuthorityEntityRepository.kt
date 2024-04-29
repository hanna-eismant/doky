package org.hkurh.doky.users.db

import org.hkurh.doky.security.UserAuthority
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

/**
 * This interface represents a repository for managing [AuthorityEntity].
 * It extends the [CrudRepository] interface for basic CRUD operations
 * and the [JpaSpecificationExecutor] interface for querying using specifications.
 */
interface AuthorityEntityRepository : CrudRepository<AuthorityEntity, Long>, JpaSpecificationExecutor<AuthorityEntity> {

    fun findByAuthority(authority: UserAuthority): AuthorityEntity?
}
