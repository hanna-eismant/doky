package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

/**
 * Represents a service for storing and retrieving files.
 */
interface FileStorageService {
    /**
     * Stores the given file at the specified file path.
     *
     * @param file The file to store.
     * @param filePath The path where the file should be stored. If null, a default path will be used.
     * @return The path where the file is stored.
     * @throws IOException If an I/O error occurs while storing the file.
     */
    @Throws(IOException::class)
    fun store(file: MultipartFile, filePath: String?): String

    /**
     * Retrieves the path of the file at the specified file path.
     *
     * @param filePath The path of the file to retrieve.
     * @return The [Path] object representing the retrieved file, or null if the file is not found.
     * @throws IOException If an I/O error occurs while retrieving the file.
     */
    @Throws(IOException::class)
    fun getFile(filePath: String): Path?
}