package org.hkurh.doky.documents.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.security.DokySecurityService
import org.springframework.stereotype.Service

@Service
class DefaultDocumentService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val dokySecurityService: DokySecurityService,
) : DocumentService {

    override fun create(name: String, description: String?): DocumentEntity {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = dokySecurityService.getCurrentUser()
        document.creator = currentUser
        val savedDocument = documentEntityRepository.save(document)
        LOG.debug { "Created new Document [${savedDocument.id}] by User [${currentUser.id}]" }
        return savedDocument
    }

    override fun find(id: Long): DocumentEntity? {
        val currentUser = dokySecurityService.getCurrentUser()
        return documentEntityRepository.findByIdAndCreatorId(id, currentUser.id)
    }

    override fun find(): List<DocumentEntity> {
        val currentUser = dokySecurityService.getCurrentUser()
        return documentEntityRepository.findByCreatorId(currentUser.id)
    }

    override fun save(document: DocumentEntity) {
        documentEntityRepository.save(document)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
