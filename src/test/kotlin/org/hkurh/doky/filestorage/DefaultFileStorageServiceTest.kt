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

package org.hkurh.doky.filestorage

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.filestorage.impl.DefaultFileStorageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.core.env.Environment
import org.springframework.mock.web.MockMultipartFile

@DisplayName("DefaultFileStorageService unit test")
class DefaultFileStorageServiceTest : DokyUnitTest {
    private val basePath = "test/storage"
    private val uploadedFileName = "test-file.txt"
    private val uploadedFilePath = "$basePath/test-file.txt"

    private val environment: Environment = mock()
    private val fileStorage: FileStorage = mock()
    private var fileStorageService = DefaultFileStorageService(environment, fileStorage)

    @Test
    @DisplayName("Should override file when upload existed")
    fun shouldOverrideFile_whenUploadExisted() {
        // given
        val file = MockMultipartFile(uploadedFileName, uploadedFileName, null, "file content".byteInputStream())
        whenever(fileStorage.checkExistence(any())).thenReturn(true)

        // when
        val storedPath = fileStorageService.store(file, uploadedFilePath)

        // then
        assertEquals(uploadedFilePath, storedPath, "Path for existed document was changed")
        verify(fileStorage).saveFile(file, uploadedFilePath)
    }

    @Test
    @DisplayName("Should store new file when upload non existing")
    fun shouldStoreNewFile_whenUploadNonExisted() {
        // given
        val file = MockMultipartFile(uploadedFileName, uploadedFileName, null, "file content".byteInputStream())
        whenever(fileStorage.checkExistence(any())).thenReturn(false)
        whenever(environment.getProperty(any(), any<String>())).thenReturn(basePath)

        // when
        val storedPath = fileStorageService.store(file, uploadedFilePath)

        // then
        assertTrue(storedPath.startsWith(basePath), "Path to file should start from base path")
        verify(fileStorage).saveFile(eq(file), any(), any())
    }
}
