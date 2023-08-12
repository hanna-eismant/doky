package org.hkurh.doky.documents

import org.springframework.core.io.Resource
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface DocumentFacade {
    fun createDocument(@NonNull name: String?, description: String?): DocumentDto?
    fun findDocument(@NonNull id: String?): DocumentDto?
    fun findAllDocuments(): List<DocumentDto?>?
    fun saveFile(@NonNull file: MultipartFile?, @NonNull id: String?)

    @Nullable
    @Throws(IOException::class)
    fun getFile(@NonNull id: String?): Resource?
}
