package org.hkurh.doky.search

import org.apache.commons.logging.LogFactory
import org.apache.solr.client.solrj.SolrClient
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocumentIndexService(
    var documentEntityRepository: DocumentEntityRepository,
    var solrClient: SolrClient
) {
    @Value("\${doky.search.solr.core.documents:documents}")
    lateinit var coreName: String

    fun fullIndex() {
        documentEntityRepository.findAll()
            .mapNotNull { it?.toSolrBean() }
            .let {
                LOG.debug("[Full Index] Add [${it.size}] documents to index")
                push(it)
            }
    }

    fun updateIndex(runDate: Date) {
        documentEntityRepository.findLatestModified(runDate)
            .map { it.toSolrBean() }
            .let {
                LOG.debug("[Update Index] Add [${it.size}] documents to index")
                push(it)
            }
    }

    private fun push(docs: List<DocumentBean>) {
        if (docs.isNotEmpty()) {
            solrClient.addBeans(coreName, docs)
            solrClient.commit(coreName)
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(DocumentIndexService::class.java)
    }
}
