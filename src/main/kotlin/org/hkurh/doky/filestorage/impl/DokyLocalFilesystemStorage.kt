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
import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.filestorage.FileStorage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
@ConditionalOnProperty(name = ["doky.filestorage.type"], havingValue = "local-filesystem", matchIfMissing = true)
class DokyLocalFilesystemStorage : FileStorage {
    @Throws(IOException::class)
    override fun saveFile(file: MultipartFile, filePathWithName: String) {
        val path = Paths.get(filePathWithName)
        saveFileToFilesystem(file, path)
    }

    @Throws(IOException::class)
    override fun saveFile(file: MultipartFile, filePath: String, fileName: String) {
        val folder = Paths.get(filePath)
        Files.createDirectories(folder)
        val path = folder.resolve(fileName)
        saveFileToFilesystem(file, path)
    }

    override fun getFile(filePath: String): Path? {
        val file = Paths.get(filePath)
        return if (Files.exists(file)) file else null
    }

    override fun checkExistence(filePath: String?): Boolean {
        return if (StringUtils.isBlank(filePath)) false else {
            val file = Paths.get(filePath!!)
            Files.exists(file)
        }
    }

    override fun deleteFile(filePath: String) {
        val file = Paths.get(filePath)
        Files.deleteIfExists(file)
    }

    @Throws(IOException::class)
    private fun saveFileToFilesystem(file: MultipartFile, path: Path) {
        Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
        LOG.debug { "Save uploaded file to ${path.toAbsolutePath()}" }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
