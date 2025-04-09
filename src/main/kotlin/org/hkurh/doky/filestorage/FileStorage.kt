/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
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

package org.hkurh.doky.filestorage

import org.springframework.web.multipart.MultipartFile
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
     */
    fun saveFile(file: MultipartFile, filePath: String, fileName: String)

    /**
     * Saves the provided file to the specified filePath with the given fileName.
     *
     * @param file The file to be saved.
     * @param filePathWithName The path and name of the file (full file path including file name).
     */
    fun saveFile(file: MultipartFile, filePathWithName: String)

    /**
     * Retrieves the file at the specified filePath as a Path object.
     *
     * @param filePath The path of the file to retrieve.
     * @return A Path object representing the file at the specified filePath, or null if the file doesn't exist.
     */
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
     */
    fun deleteFile(filePath: String)
}
