package org.hkurh.doky.filestorage.impl

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobContainerClientBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.filestorage.FileStorage
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path

@Service
@ConditionalOnProperty(name = ["doky.filestorage.type"], havingValue = "azure-blob", matchIfMissing = false)
class DokyAzureBlobStorage : FileStorage {
    @Value("\${doky.filestorage.azure.connection}")
    private val connectionString: String? = null

    @Value("\${doky.filestorage.azure.container}")
    lateinit var containerName: String
    private var blobContainerClient: BlobContainerClient? = null

    @PostConstruct
    fun init() {
        LOG.debug { "Azure Blob container name [$containerName] initialized" }
        blobContainerClient = BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient()
    }

    @Throws(IOException::class)
    override fun saveFile(file: MultipartFile, filePathWithName: String) {
        val blobClient = blobContainerClient!!.getBlobClient(filePathWithName)
        blobClient.upload(file.inputStream)
    }

    @Throws(IOException::class)
    override fun saveFile(file: MultipartFile, filePath: String, fileName: String) {
        saveFile(file, filePath + fileName)
    }

    @Throws(IOException::class)
    override fun getFile(filePath: String): Path {
        val blobClient = blobContainerClient!!.getBlobClient(filePath)
        val now = DateTime()
        val prefix = now.toString("MM-dd-yyyy") + "-"
        val suffix = "." + FilenameUtils.getExtension(filePath)
        val tempFile = File.createTempFile(prefix, suffix)
        val tempFileOutStream = FileOutputStream(tempFile)
        blobClient.downloadStream(tempFileOutStream)
        return tempFile.toPath()
    }

    override fun checkExistence(filePath: String?): Boolean {
        return if (StringUtils.isBlank(filePath)) false else {
            val blobClient = blobContainerClient!!.getBlobClient(filePath)
            blobClient.exists()
        }
    }

    override fun deleteFile(filePath: String) {
        val blobClient = blobContainerClient!!.getBlobClient(filePath)
        blobClient.deleteIfExists()
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
