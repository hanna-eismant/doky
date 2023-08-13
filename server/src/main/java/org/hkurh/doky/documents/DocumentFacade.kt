package org.hkurh.doky.documents

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface DocumentFacade {
    fun createDocument(name: String, description: String?): DocumentDto?
    fun findDocument(id: String): DocumentDto?
    fun findAllDocuments(): List<DocumentDto?>
    fun saveFile(file: MultipartFile, id: String)
    @Throws(IOException::class)
    fun getFile(id: String): Resource?
}
