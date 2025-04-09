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

package org.hkurh.doky.filestorage.impl

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobContainerClientBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.hkurh.doky.filestorage.FileStorage
import org.hkurh.doky.getExtension
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path

@Service
@ConditionalOnProperty(name = ["doky.filestorage.type"], havingValue = "azure-blob", matchIfMissing = false)
class DokyAzureBlobStorage : FileStorage {

    private val log = KotlinLogging.logger {}
    @Value("\${doky.filestorage.azure.connection}")
    private lateinit var connectionString: String
    @Value("\${doky.filestorage.azure.container}")
    private lateinit var containerName: String
    private lateinit var blobContainerClient: BlobContainerClient

    @PostConstruct
    fun init() {
        log.debug { "Azure Blob container name [$containerName] initialized" }
        blobContainerClient = BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient()
    }

    override fun saveFile(file: MultipartFile, filePathWithName: String) {
        val blobClient = blobContainerClient.getBlobClient(filePathWithName)
        blobClient.upload(file.inputStream)
    }

    override fun saveFile(file: MultipartFile, filePath: String, fileName: String) {
        saveFile(file, filePath + fileName)
    }

    override fun getFile(filePath: String): Path {
        val blobClient = blobContainerClient.getBlobClient(filePath)
        val now = DateTime()
        val prefix = now.toString("MM-dd-yyyy") + "-"
        val suffix = ".${filePath.getExtension()}"
        val tempFile = File.createTempFile(prefix, suffix)
        val tempFileOutStream = FileOutputStream(tempFile)
        blobClient.downloadStream(tempFileOutStream)
        return tempFile.toPath()
    }

    override fun checkExistence(filePath: String?): Boolean {
        return if (filePath.isNullOrBlank()) false else {
            val blobClient = blobContainerClient.getBlobClient(filePath)
            blobClient.exists()
        }
    }

    override fun deleteFile(filePath: String) {
        val blobClient = blobContainerClient.getBlobClient(filePath)
        blobClient.deleteIfExists()
    }
}
