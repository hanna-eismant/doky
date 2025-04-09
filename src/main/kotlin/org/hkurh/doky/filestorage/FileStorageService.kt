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
 * Represents a service for storing and retrieving files.
 */
interface FileStorageService {

    /**
     * Stores the given file at the specified file path.
     *
     * @param file The file to store.
     * @param filePath The path where the file should be stored. If null, a default path will be used.
     * @return The path where the file is stored.
     */
    fun storeFile(file: MultipartFile, filePath: String?): String

    /**
     * Retrieves the path of the file at the specified file path.
     *
     * @param filePath The path of the file to retrieve.
     * @return The [Path] object representing the retrieved file, or null if the file is not found.
     */
    fun getFile(filePath: String): Path?

    /**
     * Deletes a file at the specified file path.
     *
     * @param filePath The path of the file to be deleted.
     */
    fun deleteFile(filePath: String)
}
