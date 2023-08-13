package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

interface FileStorageService {
    /**
     * Save file to storage
     *
     * @return filepath that can be used to download file from storage by [.getFile] method
     * @throws IOException
     */
    @Throws(IOException::class)
    fun store(file: MultipartFile, filePath: String?): String

    /**
     * Get file from storage
     *
     * @param filePath filepath to download. It is returned by [.store] method
     * @return
     */
    @Throws(IOException::class)
    fun getFile(filePath: String): Path?
}
