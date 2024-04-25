package org.hkurh.doky.documents

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DocumentService(private val documentEntityRepository: DocumentEntityRepository, private val userService: UserService) {
    fun create(name: String, description: String?): DocumentEntity {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = userService.getCurrentUser()
        document.creator = currentUser
        LOG.debug("Created new Document ${document.id} by User ${currentUser.id}")
        return documentEntityRepository.save(document)
    }

    fun find(id: String): DocumentEntity? {
        val documentId = id.toLong()
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByIdAndCreatorId(documentId, currentUser.id)
    }

    fun find(): List<DocumentEntity> {
        val currentUser = userService.getCurrentUser()
        return documentEntityRepository.findByCreatorId(currentUser.id)
    }

    fun save(document: DocumentEntity) {
        documentEntityRepository.save(document)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentService::class.java)
    }
}
