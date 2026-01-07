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

package org.hkurh.doky.documents.impl

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.DownloadTokenService
import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.hkurh.doky.search.DocumentSearchService
import org.hkurh.doky.users.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("DefaultUserFacade unit test")
class DefaultDocumentFacadeTest : DokyUnitTest {

    private var documentService: DocumentService = mock()
    private var downloadTokenService: DownloadTokenService = mock()
    private var fileStorageService: FileStorageService = mock()
    private var userService: UserService = mock()
    private var documentSearchService: DocumentSearchService = mock()
    private var documentFacade =
        DefaultDocumentFacade(documentService, downloadTokenService, fileStorageService, userService, documentSearchService)

    @BeforeEach
    fun setUp() {
        whenever(fileStorageService.storeFile(any(), any())).thenReturn("test/path")
    }

    @Test
    @DisplayName("Should update Document when it exists")
    fun shouldUpdateDocument_whenItExists() {
        // given
        val originId: Long = 1
        val originDocument = DocumentEntity().apply {
            id = originId
            name = "Test"
            description = "Description"
        }
        val updatedDocument = DocumentRequest().apply {
            name = "Another Name"
            description = "Description for Document"
        }
        whenever(documentService.find(originId)).thenReturn(originDocument)

        // when
        assertDoesNotThrow { documentFacade.update(originId, updatedDocument) }

        // then
        verify(documentService).save(originDocument)
        assertAll(
            "Document properties",
            { assertEquals(updatedDocument.name, originDocument.name, "Name for Document is not updated") },
            {
                assertEquals(
                    updatedDocument.description, originDocument.description,
                    "Description for Document is not updated"
                )
            }
        )
    }

    @Test
    @DisplayName("Should throw exception when update non existing document")
    fun shouldThrowException_whenUpdateNonExistingDocument() {
        // given
        val originId: Long = 1
        val updatedDocument = DocumentRequest().apply {
            name = "Another Name"
            description = "Description for Document"
        }
        whenever(documentService.find(originId)).thenReturn(null)

        // when - then
        assertThrows<DokyNotFoundException> { documentFacade.update(originId, updatedDocument) }
    }
}
