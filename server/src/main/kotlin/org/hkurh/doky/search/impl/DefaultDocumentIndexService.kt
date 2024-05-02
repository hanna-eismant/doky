package org.hkurh.doky.search.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.SolrQuery
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentIndexService
import org.hkurh.doky.search.solr.DocumentIndexBean
import org.hkurh.doky.search.solr.DocumentResultBean
import org.hkurh.doky.search.toSolrIndexBean
import org.hkurh.doky.search.toSolrResultBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*


@Service
class DefaultDocumentIndexService(
    var documentEntityRepository: DocumentEntityRepository,
    var solrClient: SolrClient
) : DocumentIndexService {
    @Value("\${doky.search.solr.core.documents:documents}")
    lateinit var coreName: String

    override fun fullIndex() {
        solrClient.deleteByQuery(coreName, "*:*")
        documentEntityRepository.findAll()
            .mapNotNull { it?.toSolrIndexBean() }
            .let {
                LOG.debug { "Full Index: Add [${it.size}] documents to index" }
                push(it)
            }
    }

    override fun updateIndex(runDate: Date) {
        documentEntityRepository.findLatestModified(runDate)
            .map { it.toSolrIndexBean() }
            .let {
                LOG.debug { "Update Index: Add [${it.size}] documents to index" }
                push(it)
            }
    }

    override fun search(query: String): List<DocumentResultBean> {
        val s = fuzzyQuery(query)
        val solrQuery = SolrQuery().apply {
            set("q", s)
            set("qf", "name^10 description^5 ")
            set("pf", "name^50 description^10 ")
            set("q.op", "OR")
            set("defType", "edismax")
        }
        val results = solrClient.query(coreName, solrQuery).results
        LOG.debug { "Solr found [${results.numFound}] results for query [$query]" }
        return if (results.numFound > 0) {
            results.map { it.toSolrResultBean() }
        } else {
            emptyList()
        }
    }

    override fun updateDocumentInfo(document: DocumentEntity) {
        push(document.toSolrIndexBean())
    }

    private fun fuzzyQuery(query: String): String {
        val replace = query.replace(" ", "~ ")
        return "$replace~"
    }

    private fun push(docs: List<DocumentIndexBean>) {
        if (docs.isNotEmpty()) {
            solrClient.addBeans(coreName, docs)
            solrClient.commit(coreName)
        }
    }

    private fun push(doc: DocumentIndexBean) {
        LOG.debug { "Pushing doc [${doc.id}] to index" }
        solrClient.addBean(coreName, doc)
        solrClient.commit(coreName)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
