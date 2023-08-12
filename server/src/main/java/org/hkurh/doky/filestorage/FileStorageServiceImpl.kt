package org.hkurh.doky.filestorage

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.logging.LogFactory
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.core.env.Environment
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Path

@Service
class FileStorageServiceImpl(private val environment: Environment, private val fileStorage: FileStorage) : FileStorageService {
    @Throws(IOException::class)
    override fun store(@NonNull file: MultipartFile?, @NonNull filePath: String?): String? {
        val isFileExists = fileStorage.checkExistence(filePath)
        return if (isFileExists) {
            fileStorage.saveFile(file, filePath)
            filePath
        } else {
            val extension = FilenameUtils.getExtension(file!!.originalFilename)
            val storagePath = storagePath
            val fileName = generateFileName(extension)
            val generatedFilePath = storagePath + fileName
            fileStorage.saveFile(file, storagePath, fileName)
            generatedFilePath
        }
    }

    @Throws(IOException::class)
    override fun getFile(@NonNull filePath: String?): Path? {
        return fileStorage.getFile(filePath)
    }

    private val storagePath: String
        private get() {
            val basePath = environment.getProperty(STORAGE_PATH_PROPERTY, DEFAULT_STORAGE_PATH)
            val today = DateTime.now(DateTimeZone.getDefault())
            return (basePath + File.separator
                    + today.year + File.separator
                    + today.monthOfYear + File.separator)
        }

    private fun generateFileName(extension: String): String {
        val randomName = RandomStringUtils.random(10, true, true)
        return "$randomName.$extension"
    }

    companion object {
        private val LOG = LogFactory.getLog(FileStorageServiceImpl::class.java)
        const val STORAGE_PATH_PROPERTY = "doky.filestorage.path"
        const val DEFAULT_STORAGE_PATH = "./mediadata"
    }
}
