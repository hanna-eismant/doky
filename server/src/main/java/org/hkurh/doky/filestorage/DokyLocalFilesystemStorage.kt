package org.hkurh.doky.filestorage

import org.apache.commons.lang3.StringUtils
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.lang.NonNull
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
    override fun saveFile(@NonNull file: MultipartFile?, @NonNull filePathWithName: String?) {
        val path = Paths.get(filePathWithName)
        saveFileToFilesystem(file, path)
    }

    @Throws(IOException::class)
    override fun saveFile(@NonNull file: MultipartFile?, @NonNull filePath: String?, @NonNull fileName: String?) {
        val folder = Paths.get(filePath)
        Files.createDirectories(folder)
        val path = folder.resolve(fileName)
        saveFileToFilesystem(file, path)
    }

    override fun getFile(@NonNull filePath: String?): Path? {
        val file = Paths.get(filePath)
        return if (Files.exists(file)) {
            file
        } else {
            null
        }
    }

    override fun checkExistence(filePath: String?): Boolean {
        if (StringUtils.isBlank(filePath)) {
            return false
        }
        val file = Paths.get(filePath)
        return Files.exists(file)
    }

    companion object {
        private val LOG = LogFactory.getLog(DokyLocalFilesystemStorage::class.java)

        @Throws(IOException::class)
        private fun saveFileToFilesystem(file: MultipartFile?, path: Path) {
            Files.copy(file!!.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
            LOG.debug(String.format("Save uploaded file to [%s]", path.toAbsolutePath()))
        }
    }
}
