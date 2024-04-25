package org.hkurh.doky.search

import org.apache.commons.logging.LogFactory
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.toDto
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component
import java.util.*

@Component
class DocumentSearchFacade(
    private val userService: UserService,
    private val documentEntityRepository: DocumentEntityRepository,
    private val documentIndexService: DocumentIndexService
) {

    fun search(query: String): List<DocumentResponse> {
        val documentIdList = documentIndexService.search(query)
            .mapNotNull { it.id?.toLong() }
        return if (documentIdList.isEmpty()) Collections.emptyList()
        else {
            val userId = userService.getCurrentUser().id
            val documents = documentEntityRepository.findByListIdAndUserId(documentIdList, userId)
                .map { it.toDto() }
            LOG.debug("Return [${documents.size}] results for query [$query]")
            documents
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(DocumentSearchFacade::class.java)
    }
}
