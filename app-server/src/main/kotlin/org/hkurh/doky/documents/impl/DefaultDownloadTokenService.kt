package org.hkurh.doky.documents.impl

import org.hkurh.doky.documents.DocumentService
import org.hkurh.doky.documents.DownloadTokenService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntity
import org.hkurh.doky.documents.db.DownloadDocumentTokenEntityRepository
import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.security.DokySecurityService
import org.hkurh.doky.security.JwtProvider
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultDownloadTokenService(
    private val documentService: DocumentService,
    private val dokySecurityService: DokySecurityService,
    private val downloadDocumentTokenEntityRepository: DownloadDocumentTokenEntityRepository,
    private val jwtProvider: JwtProvider
) : DownloadTokenService {

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

    override fun validateDownloadTokenAndFetchDocument(documentId: Long, token: String): DocumentEntity {
        try {
            if (!jwtProvider.isDownloadTokenValid(token)) throw DokyInvalidTokenException("Token is expired or invalid")
        } catch (e: Exception) {
            throw DokyInvalidTokenException("Error occurred during token validation: ${e.message}")
        }

        val user = dokySecurityService.getCurrentUser()
        val document =
            documentService.find(documentId)
                ?: throw DokyInvalidTokenException("Document with id [$documentId] not found")
        val documentToken = downloadDocumentTokenEntityRepository.findByUserAndDocument(user, document)
        if (documentToken == null || documentToken.token != token) {
            throw DokyInvalidTokenException("Token [$token] is not valid for document [$documentId] and user [${user.id}]")
        }
        return document
    }
}
