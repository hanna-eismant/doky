package org.hkurh.doky.documents

import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.documents.api.DocumentResponse
import org.springframework.core.io.Resource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface DocumentFacade {
    fun createDocument(name: String, description: String?): DocumentResponse?
    fun update(id: String, document: DocumentRequest)
    fun findDocument(id: String): DocumentResponse?
    fun findAllDocuments(): List<DocumentResponse?>

    @Transactional(propagation = Propagation.REQUIRED)
    fun saveFile(file: MultipartFile, id: String)

    @Throws(IOException::class)
    fun getFile(id: String): Resource?
}
