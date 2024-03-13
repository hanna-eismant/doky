package org.hkurh.doky.documents

import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
@DisplayName("UserFacade unit test")
class DocumentFacadeTest {

    @Spy
    @InjectMocks
    private val documentFacade: DocumentFacade? = null
    private val documentService: DocumentService = mock()
    private val fileStorageService: FileStorageService = mock()

    @BeforeEach
    fun setUp() {
        whenever(fileStorageService.store(any(), any())).thenReturn("test/path")
    }

    @Test
    @DisplayName("Should update Document when it exists")
    fun shouldUpdateDocument_whenItExists() {
        // given
        val originId = "1"
        val originDocument = DocumentEntity().apply {
            id = originId.toLong()
            name = "Test"
            description = "Description"
        }
        val updatedDocument = DocumentRequest("Another Name", "Description for Document")
        whenever(documentService.find(originId)).thenReturn(originDocument)

        // when
        assertDoesNotThrow { documentFacade?.update(originId, updatedDocument) }

        // then
        verify(documentService).save(originDocument)
        assertAll(
            "Document properties",
            { assertEquals(updatedDocument.name, originDocument.name, "Name for Document is not updated") },
            { assertEquals(updatedDocument.description, originDocument.description,
                    "Description for Document is not updated") }
        )
    }


    @Test
    @DisplayName("Should throw exception when update non existing document")
    fun shouldThrowException_whenUpdateNonExistingDocument() {
        // given
        val originId = "1"
        val updatedDocument = DocumentRequest("Another Name", "Description for Document")
        whenever(documentService.find(originId)).thenReturn(null)

        // when - then
        assertThrows<DokyNotFoundException> { documentFacade?.update(originId, updatedDocument) }
    }
}
