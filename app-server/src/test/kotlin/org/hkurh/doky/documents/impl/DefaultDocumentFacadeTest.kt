package org.hkurh.doky.documents.impl

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.DownloadTokenService
import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.hkurh.doky.security.DokySecurityService
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
    private var dokySecurityService: DokySecurityService = mock()
    private var documentFacade =
        DefaultDocumentFacade(documentService, downloadTokenService, fileStorageService, dokySecurityService)

    @BeforeEach
    fun setUp() {
        whenever(fileStorageService.store(any(), any())).thenReturn("test/path")
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
