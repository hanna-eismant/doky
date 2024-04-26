package org.hkurh.doky.documents.impl

import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DefaultDocumentService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val userService: UserService
) :
    DocumentService {

    override fun create(name: String, description: String?): DocumentEntity {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = userService.getCurrentUser()
        document.creator = currentUser
        val savedDocument = documentEntityRepository.save(document)
        LOG.debug("Created new Document [${savedDocument.id}] by User [${currentUser.id}]")
        return savedDocument
    }

    override fun find(id: String): DocumentEntity? {
        val documentId = id.toLong()
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByIdAndCreatorId(documentId, currentUser.id)
    }

    override fun find(): List<DocumentEntity> {
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByCreatorId(currentUser.id)
    }

    override fun save(document: DocumentEntity) {
        documentEntityRepository.save(document)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentService::class.java)
    }
}
