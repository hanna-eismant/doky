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

package org.hkurh.doky.maintenance.impl

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.users.db.UserEntityRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

@DisplayName("DefaultTestUserService unit test")
class DefaultTestUserServiceTest : DokyUnitTest {

    private val documentEntityRepository: DocumentEntityRepository = mock()
    private val userEntityRepository: UserEntityRepository = mock()

    @Test
    @DisplayName("Should delete test data")
    fun shouldDeleteTestData() {
        // given
        val testEmailPrefix = "doky.test"
        val defaultTestUserService = DefaultTestUserService(
            testEmailPrefix,
            documentEntityRepository,
            userEntityRepository
        )

        //when
        defaultTestUserService.cleanupTestUsers()

        //then
        verify(documentEntityRepository).deleteAllByUserPrefix(testEmailPrefix)
        verify(userEntityRepository).deleteByUidPrefix(testEmailPrefix)
    }

    @Test
    @DisplayName("Should not delete test data for empty prefix")
    fun shouldNotDeleteTestData_ifEmptyPrefix() {
        //given
        val defaultTestUserService = DefaultTestUserService(
            "",
            documentEntityRepository,
            userEntityRepository
        )

        //when
        assertThrows<IllegalArgumentException> { defaultTestUserService.cleanupTestUsers() }

        //then
        verifyNoInteractions(documentEntityRepository)
        verifyNoInteractions(userEntityRepository)
    }
}
