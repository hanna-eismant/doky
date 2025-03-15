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
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.filestorage.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.RandomStringUtils
import org.hkurh.doky.filestorage.FileStorage
import org.hkurh.doky.filestorage.FileStorageService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File.separator
import java.io.IOException
import java.nio.file.Path

@Service
class DefaultFileStorageService(
    private val environment: Environment,
    @Suppress("SpringJavaInjectionPointsAutowiringInspection") private val fileStorage: FileStorage
) : FileStorageService {
    @Throws(IOException::class)
    override fun store(file: MultipartFile, filePath: String?): String {
        val isFileExists = fileStorage.checkExistence(filePath)
        return if (isFileExists) {
            val updatedFilePath = checkFileExtension(file, filePath!!)
            fileStorage.deleteFile(filePath)
            fileStorage.saveFile(file, updatedFilePath)
            updatedFilePath
        } else {
            val extension = FilenameUtils.getExtension(file.originalFilename)
            val storagePath = generateStoragePath()
            val fileName = generateFileName(extension)
            val generatedFilePath = storagePath + fileName
            fileStorage.saveFile(file, storagePath, fileName)
            generatedFilePath
        }
    }

    @Throws(IOException::class)
    override fun getFile(filePath: String): Path? {
        return fileStorage.getFile(filePath)
    }

    private fun generateStoragePath(): String {
        val basePath = environment.getProperty(STORAGE_PATH_PROPERTY, DEFAULT_STORAGE_PATH)
        val today = DateTime.now(DateTimeZone.getDefault())
        return "$basePath$separator${today.year}$separator${today.monthOfYear}$separator"
    }

    private fun generateFileName(extension: String): String {
        val randomName = RandomStringUtils.random(10, true, true)
        return "$randomName.$extension"
    }

    private fun checkFileExtension(file: MultipartFile, filePath: String): String {
        val uploadExt = FilenameUtils.getExtension(file.originalFilename)
        val savedExt = FilenameUtils.getExtension(filePath)
        LOG.debug { "Saved extension [$savedExt], upload extension [$uploadExt]" }
        return if (savedExt != uploadExt) filePath.replace(".$savedExt", ".$uploadExt")
        else filePath
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
        const val STORAGE_PATH_PROPERTY = "doky.filestorage.path"
        const val DEFAULT_STORAGE_PATH = "./mediadata"
    }
}
