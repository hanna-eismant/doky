package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

/**
 * FileStorage interface for managing file operations.
 */
interface FileStorage {
    /**
     * Saves the provided file to the specified filePath with the given fileName.
     *
     * @param file The file to be saved.
     * @param filePath The path where the file should be saved.
     * @param fileName The name of the file.
     * @throws IOException If there is an error while saving the file.
     */
    @Throws(IOException::class)
    fun saveFile(file: MultipartFile, filePath: String, fileName: String)

    /**
     * Saves the provided file to the specified filePath with the given fileName.
     *
     * @param file The file to be saved.
     * @param filePathWithName The path and name of the file (full file path including file name).
     * @throws IOException If there is an error while saving the file.
     */
    @Throws(IOException::class)
    fun saveFile(file: MultipartFile, filePathWithName: String)

    /**
     * Retrieves the file at the specified filePath as a Path object.
     *
     * @param filePath The path of the file to retrieve.
     * @return A Path object representing the file at the specified filePath, or null if the file doesn't exist.
     * @throws IOException If an error occurs while attempting to retrieve the file.
     * @throws NullPointerException If the filePath parameter is null.
     */
    @Throws(IOException::class)
    fun getFile(filePath: String): Path?

    /**
     * Checks the existence of a file at the specified filePath.
     *
     * @param filePath The path of the file to check.
     * @return true if the file exists, false otherwise.
     */
    fun checkExistence(filePath: String?): Boolean

    /**
     * Deletes a file at the specified filePath.
     *
     * @param filePath The path of the file to be deleted.
     * @throws IOException If an error occurs while deleting the file.
     * @throws NullPointerException If the filePath parameter is null.
     */
    fun deleteFile(filePath: String)
}
