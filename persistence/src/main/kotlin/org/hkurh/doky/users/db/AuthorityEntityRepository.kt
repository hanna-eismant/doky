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
