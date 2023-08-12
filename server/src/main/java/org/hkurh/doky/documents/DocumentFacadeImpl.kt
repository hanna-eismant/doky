package org.hkurh.doky.documents

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.MapperFactory.Companion.modelMapper
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.filestorage.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.stream.Collectors

@Component
class DocumentFacadeImpl(private val documentService: DocumentService, private val fileStorageService: FileStorageService) : DocumentFacade {
    override fun createDocument(@NonNull name: String?, description: String?): DocumentDto? {
        val documentEntity = documentService.create(name, description)
        LOG.debug(String.format("Created new Document [%s]", documentEntity!!.id))
        return modelMapper.map(documentEntity, DocumentDto::class.java)
    }

    override fun findDocument(@NonNull id: String?): DocumentDto? {
        val documentEntity = documentService.find(id)
        return documentEntity
                ?.map { entity: DocumentEntity? -> modelMapper.map(entity, DocumentDto::class.java) }
                ?.orElse(null)
    }

    override fun findAllDocuments(): List<DocumentDto?>? {
        val documentEntityList = documentService.find()
        return documentEntityList!!.stream()
                .map { entity -> modelMapper.map(entity, DocumentDto::class.java) }
                .collect(Collectors.toList())
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun saveFile(@NonNull file: MultipartFile?, @NonNull id: String?) {
        val documentOpt = documentService.find(id)
        if (documentOpt!!.isPresent) {
            try {
                val document = documentOpt.get()
                val path = fileStorageService.store(file, document.filePath)
                document.filePath = path
                documentService.save(document)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        } else {
            throw DokyNotFoundException(String.format("Document with id [%s] not found", id))
        }
    }

    @Nullable
    @Throws(IOException::class)
    override fun getFile(@NonNull id: String?): Resource? {
        val documentOpt = documentService.find(id)
        return if (documentOpt!!.isPresent && StringUtils.isNotBlank(documentOpt.get().filePath)) {
            val filePath = documentOpt.get().filePath
            val file = fileStorageService.getFile(filePath)
            if (file != null) {
                LOG.debug(String.format("Download file for Document [%s] with URI [%s]", id, file.toUri()))
                UrlResource(file.toUri())
            } else {
                LOG.warn(String.format("File [%s] attached to document [%s] does not exists in storage", filePath, id))
                null
            }
        } else {
            LOG.debug(String.format("No attached file for Document [%s]", id))
            null
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentFacadeImpl::class.java)
    }
}
