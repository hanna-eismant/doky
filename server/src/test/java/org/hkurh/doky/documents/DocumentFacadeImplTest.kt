package org.hkurh.doky.documents

import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
@DisplayName("UserFacade unit test")
class DocumentFacadeImplTest {

    @Spy
    @InjectMocks
    private val documentFacade: DocumentFacadeImpl? = null

    @Mock
    private val documentService: DocumentService? = null

    @Mock
    private val fileStorageService: FileStorageService? = null

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
        whenever(documentService?.find(originId)).thenReturn(originDocument)

        // when
        assertDoesNotThrow { documentFacade?.update(originId, updatedDocument) }

        // then
        verify(documentService)?.save(originDocument)
        assertAll(
            "Document properties",
            { assertEquals(updatedDocument.name, originDocument.name) },
            { assertEquals(updatedDocument.description, originDocument.description) }
        )
    }


    @Test
    @DisplayName("Should throw exception when update non existing document")
    fun shouldThrowException_whenUpdateNonExistingDocument() {
        // given
        val originId = "1"
        val updatedDocument = DocumentRequest("Another Name", "Description for Document")
        whenever(documentService?.find(originId)).thenReturn(null)

        // when
        assertThrows<DokyNotFoundException> { documentFacade?.update(originId, updatedDocument) }
    }
}
