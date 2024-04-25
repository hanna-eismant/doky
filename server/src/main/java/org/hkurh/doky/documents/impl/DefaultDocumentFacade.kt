package org.hkurh.doky.documents.impl

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.documents.DocumentFacade
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.api.DocumentRequest
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.hkurh.doky.toDto
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Component
class DefaultDocumentFacade(
    private val documentService: DocumentService,
    private val fileStorageService: FileStorageService
) : DocumentFacade {
    override fun createDocument(name: String, description: String?): DocumentResponse? {
        val documentEntity = documentService.create(name, description)
        LOG.debug("Created new Document with id [${documentEntity.id}]")
        return documentEntity.toDto()
    }

    override fun update(id: String, document: DocumentRequest) {
        val existedDocument =
            documentService.find(id) ?: throw DokyNotFoundException("Document with id [$id] not found")
        existedDocument.apply {
            name = document.name
            description = document.description
            documentService.save(this)
        }
    }

    override fun findDocument(id: String): DocumentResponse? {
        return documentService.find(id)?.toDto()
    }

    override fun findAllDocuments(): List<DocumentResponse?> {
        return documentService.find().map { it.toDto() }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun saveFile(file: MultipartFile, id: String) {
        val document = documentService.find(id)
        if (document != null) {
            try {
                val path = fileStorageService.store(file, document.filePath)
                document.filePath = path
                document.fileName = file.originalFilename
                documentService.save(document)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            throw DokyNotFoundException("Document with id [$id] not found")
        }
    }

    @Throws(IOException::class)
    override fun getFile(id: String): Resource? {
        val document = documentService.find(id)
        return if (StringUtils.isNotBlank(document?.filePath)) {
            val filePath = document!!.filePath
            val file = fileStorageService.getFile(filePath!!)
            if (file != null) {
                val fileUri = file.toUri()
                LOG.debug("Download file for Document [$id] with URI [$fileUri]")
                UrlResource(fileUri)
            } else {
                LOG.warn("File [$filePath] attached to document [$id] does not exists in storage")
                null
            }
        } else {
            LOG.debug("No attached file for Document [$id]")
            null
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DefaultDocumentFacade::class.java)
    }
}
