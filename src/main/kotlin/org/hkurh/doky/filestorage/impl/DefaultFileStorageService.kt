/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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

package org.hkurh.doky.filestorage.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.filestorage.FileStorage
import org.hkurh.doky.filestorage.FileStorageService
import org.hkurh.doky.generateSecureRandomString
import org.hkurh.doky.getExtension
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File.separator
import java.nio.file.Path

@Service
class DefaultFileStorageService(
    @Suppress("SpringJavaInjectionPointsAutowiringInspection") private val fileStorage: FileStorage
) : FileStorageService {

    private val log = KotlinLogging.logger {}

    @Value("\${doky.filestorage.path:./files}")
    private lateinit var basePath: String

    override fun storeFile(file: MultipartFile, filePath: String?): String {
        val isFileExists = fileStorage.checkExistence(filePath)
        return if (isFileExists) {
            val updatedFilePath = checkFileExtension(file, filePath!!)
            fileStorage.deleteFile(filePath)
            fileStorage.saveFile(file, updatedFilePath)
            updatedFilePath
        } else {
            val extension = file.originalFilename.getExtension()
            val storagePath = generateStoragePath()
            val fileName = generateFileName(extension)
            val generatedFilePath = storagePath + fileName
            fileStorage.saveFile(file, storagePath, fileName)
            generatedFilePath
        }
    }

    override fun getFile(filePath: String): Path? {
        return fileStorage.getFile(filePath)
    }

    override fun deleteFile(filePath: String) {
        fileStorage.deleteFile(filePath)
    }

    private fun generateStoragePath(): String {
        val today = DateTime.now(DateTimeZone.getDefault())
        return "$basePath$separator${today.year}$separator${today.monthOfYear}$separator"
    }

    private fun generateFileName(extension: String): String {
        val randomName = generateSecureRandomString()
        return "$randomName.$extension"
    }

    private fun checkFileExtension(file: MultipartFile, filePath: String): String {
        val uploadExt = file.originalFilename.getExtension()
        val savedExt = filePath.getExtension()
        log.debug { "Saved extension [$savedExt], upload extension [$uploadExt]" }
        return if (savedExt != uploadExt) filePath.replace(".$savedExt", ".$uploadExt")
        else filePath
    }
}
