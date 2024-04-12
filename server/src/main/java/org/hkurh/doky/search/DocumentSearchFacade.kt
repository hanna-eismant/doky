package org.hkurh.doky.search

import org.hkurh.doky.documents.api.DocumentDto
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.toDto
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component

@Component
class DocumentSearchFacade(
    private val userService: UserService ,
    private val documentEntityRepository: DocumentEntityRepository,
    private val documentIndexService: DocumentIndexService
) {

    fun search(query: String): List<DocumentDto> {
        documentIndexService.fullIndex()
        val documentIdList = documentIndexService.search(query)
            .mapNotNull { it.id?.toLong() }
        val userId = userService.getCurrentUser().id
        return documentEntityRepository.findByListIdAndUserId(documentIdList, userId)
            .map { it.toDto() }
    }
}
