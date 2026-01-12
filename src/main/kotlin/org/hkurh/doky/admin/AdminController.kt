/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

package org.hkurh.doky.admin

import org.hkurh.doky.maintenance.TestUserService
import org.hkurh.doky.search.index.IndexService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.Executors

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class AdminController(
    private val indexService: IndexService,
    private val testUserService: TestUserService
) {

    @PostMapping("/search/index/full")
    fun startFullIndex(): ResponseEntity<*> {
        Executors.newCachedThreadPool().submit { indexService.fullIndex() }
        return ResponseEntity.accepted().build<Any>()
    }

    @PostMapping("/maintenance/cleanup/test-users")
    fun cleanupTestUsers(): ResponseEntity<*> {
        Executors.newCachedThreadPool().submit { testUserService.cleanupTestUsers() }
        return ResponseEntity.accepted().build<Any>()
    }
}
