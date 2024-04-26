package org.hkurh.doky.search.impl

import org.apache.commons.logging.LogFactory
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentIndexService
import org.hkurh.doky.search.DocumentSearchFacade
import org.hkurh.doky.toDto
import org.hkurh.doky.users.impl.DefaultUserService
import org.springframework.stereotype.Component
import java.util.*

@Component
class DefaultDocumentSearchFacade(
    private val userService: DefaultUserService,
    private val documentEntityRepository: DocumentEntityRepository,
    private val documentIndexService: DocumentIndexService
) : DocumentSearchFacade {

    override fun search(query: String): List<DocumentResponse> {
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
