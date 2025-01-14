package org.hkurh.doky.documents.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntity
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntityRepository
import org.hkurh.doky.security.JwtProvider
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultDocumentService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val downloadDocumentTokenEntityRepository: DownloadDocumentTokenEntityRepository,
    private val userService: UserService,
    private val jwtProvider: JwtProvider
) : DocumentService {

    override fun create(name: String, description: String?): DocumentEntity {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = userService.getCurrentUser()
        document.creator = currentUser
        val savedDocument = documentEntityRepository.save(document)
        LOG.debug { "Created new Document [${savedDocument.id}] by User [${currentUser.id}]" }
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

    override fun generateDownloadToken(user: UserEntity, document: DocumentEntity): String {
        val token = jwtProvider.generateDownloadToken(user)
        val tokenEntity =
            downloadDocumentTokenEntityRepository.findByUserAndDocument(user, document) ?: DownloadDocumentTokenEntity()
        tokenEntity.apply {
            this.user = user
            this.document = document
            this.token = token
        }
        downloadDocumentTokenEntityRepository.save(tokenEntity)
        return token
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
