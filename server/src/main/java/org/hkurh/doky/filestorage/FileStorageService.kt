package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

interface FileStorageService {
    @Throws(IOException::class)
    fun store(file: MultipartFile, filePath: String?): String

    @Throws(IOException::class)
    fun getFile(filePath: String): Path?
}
