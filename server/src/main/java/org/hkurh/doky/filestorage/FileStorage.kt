package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

interface FileStorage {
    @Throws(IOException::class)
    fun saveFile(file: MultipartFile, filePath: String, fileName: String)

    @Throws(IOException::class)
    fun saveFile(file: MultipartFile, filePathWithName: String)

    @Throws(IOException::class)
    fun getFile(filePath: String): Path?

    /**
     * Check if file with specified path exists in storage.
     *
     * @return `true` - if file exists,
     * `false` if file does not exist or {@param filePath} is null or empty
     */
    fun checkExistence(filePath: String?): Boolean
    fun deleteFile(filePath: String)
}
