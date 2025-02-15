package org.hkurh.doky.search.index.impl

import com.azure.search.documents.SearchClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentIndexData
import org.hkurh.doky.search.index.IndexService
import org.hkurh.doky.toIndexData
import org.springframework.stereotype.Service

@Service
class DefaultIndexService(
    private val documentEntityRepository: DocumentEntityRepository,
    private val searchClient: SearchClient
) : IndexService {

    override fun fullIndex() {
        cleanIndex()
        val documents = documentEntityRepository.findAll()
            .mapNotNull { documentEntity -> documentEntity?.toIndexData() }
        val indexingResult = searchClient.uploadDocuments(documents)
        LOG.debug { "Upload [${indexingResult.results.size}] documents to index" }
        indexingResult.results.forEach {
            if (!it.isSucceeded) {
                LOG.error { "Document [${it.key}] upload failed: [${it.errorMessage}]" }
            }
        }
    }

    private fun cleanIndex() {
        val results = searchClient.search("*")
            .map { result -> result.getDocument(DocumentIndexData::class.java) }
        if (results.isNotEmpty()) {
            LOG.debug { "Deleting [${results.size}] documents from index" }
            searchClient.deleteDocuments(results)
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
