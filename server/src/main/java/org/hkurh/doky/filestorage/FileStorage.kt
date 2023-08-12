package org.hkurh.doky.filestorage

import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

interface FileStorage {
    @Throws(IOException::class)
    fun saveFile(@NonNull file: MultipartFile?, @NonNull filePath: String?, @NonNull fileName: String?)

    @Throws(IOException::class)
    fun saveFile(@NonNull file: MultipartFile?, @NonNull filePathWithName: String?)

    @Throws(IOException::class)
    fun getFile(@NonNull filePath: String?): Path?

    /**
     * Check if file with specified path exists in storage.
     *
     * @return `true` - if file exists,
     * `false` if file does not exist or {@param filePath} is null or empty
     */
    fun checkExistence(@Nullable filePath: String?): Boolean
}
