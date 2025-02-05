package org.hkurh.doky.search.index

import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.springframework.stereotype.Service

@Service
class DefaultIndexService(
    private val documentEntityRepository: DocumentEntityRepository
) : IndexService {

    override fun fullIndex() {
        documentEntityRepository.findAll()
            .forEach { documentEntity -> }
    }
}
