package org.hkurh.doky.filestorage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.core.env.Environment
import org.springframework.mock.web.MockMultipartFile

@ExtendWith(MockitoExtension::class)
@DisplayName("FileStorageService unit test")
class FileStorageServiceTest {
    private val basePath = "test/storage"
    private val uploadedFileName = "test-file.txt"
    private val uploadedFilePath = "$basePath/test-file.txt"

    @InjectMocks
    @Spy
    private val fileStorageService: FileStorageService? = null
    private val environment: Environment = mock()
    private val fileStorage: FileStorage = mock()

    @Test
    @DisplayName("Should override file when upload existed")
    fun shouldOverrideFile_whenUploadExisted() {
        // given
        val file = MockMultipartFile(uploadedFileName, "file content".byteInputStream())
        whenever(fileStorage.checkExistence(any())).thenReturn(true)

        // when
        val storedPath = fileStorageService?.store(file, uploadedFilePath)

        // then
        assertEquals(uploadedFilePath, storedPath, "Path for existed document was changed")
        verify(fileStorage).saveFile(file, uploadedFilePath)
    }

    @Test
    @DisplayName("Should store new file when upload non existing")
    fun shouldStoreNewFile_whenUploadNonExisted() {
        // given
        val file = MockMultipartFile(uploadedFileName, "file content".byteInputStream())
        whenever(fileStorage.checkExistence(any())).thenReturn(false)
        whenever(environment.getProperty(any(), any<String>())).thenReturn(basePath)

        // when
        val storedPath = fileStorageService?.store(file, uploadedFilePath)

        // then
        assertTrue(storedPath!!.startsWith(basePath), "Path to file should start from base path")
        verify(fileStorage).saveFile(eq(file), any(), any())
    }
}
