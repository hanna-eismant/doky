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
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

/**
 * Implementation of [AuditorAware] interface that retrieves the current auditor (user) from Spring Security.
 */
@Component
class SpringSecurityAuditorAware : AuditorAware<UserEntity> {

    /**
     * Retrieves the current auditor (user) from Spring Security.
     *
     * @return An [Optional] containing the [UserEntity] representing the current auditor,
     *         or an empty [Optional] if the auditor is not found.
     */
    override fun getCurrentAuditor(): Optional<UserEntity> {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).userEntity
        return Optional.ofNullable(userEntity)
    }
}
