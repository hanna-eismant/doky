package org.hkurh.doky.documents

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.MapperFactory.Companion.modelMapper
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.stream.Collectors

@Component
class DocumentFacadeImpl(private val documentService: DocumentService, private val fileStorageService: FileStorageService) : DocumentFacade {
    override fun createDocument(name: String, description: String?): DocumentDto? {
        val documentEntity = documentService.create(name, description)
        LOG.debug("Created new Document ${documentEntity.id}")
        return modelMapper.map(documentEntity, DocumentDto::class.java)
    }

    override fun findDocument(id: String): DocumentDto? {
        val documentEntity = documentService.find(id)
        return if (documentEntity == null) null else modelMapper.map(documentEntity, DocumentDto::class.java)
    }

    override fun findAllDocuments(): List<DocumentDto?> {
        val documentEntityList = documentService.find()
        return documentEntityList.stream()
                .map { entity -> modelMapper.map(entity, DocumentDto::class.java) }
                .collect(Collectors.toList())
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun saveFile(file: MultipartFile, id: String) {
        val document = documentService.find(id)
        if (document != null) {
            try {
                val path = fileStorageService.store(file, document.filePath)
                document.filePath = path
                documentService.save(document)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            throw DokyNotFoundException("Document with id $id not found")
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
        private val LOG = LoggerFactory.getLogger(DocumentFacadeImpl::class.java)
    }
}
