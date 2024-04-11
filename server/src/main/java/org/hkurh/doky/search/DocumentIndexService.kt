package org.hkurh.doky.search

import org.apache.commons.logging.LogFactory
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.SolrQuery
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.solr.DocumentIndexBean
import org.hkurh.doky.search.solr.DocumentResultBean
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
            .mapNotNull { it?.toSolrIndexBean() }
            .let {
                LOG.debug("[Full Index] Add [${it.size}] documents to index")
                push(it)
            }
    }

    fun updateIndex(runDate: Date) {
        documentEntityRepository.findLatestModified(runDate)
            .map { it.toSolrIndexBean() }
            .let {
                LOG.debug("[Update Index] Add [${it.size}] documents to index")
                push(it)
            }
    }

    fun search(query: String): List<DocumentResultBean> {
        val s = fuzzyQuery(query)
        val solrQuery = SolrQuery().apply {
            set("q", s)
            set("qf", "name^10 description^5 ")
            set("pf", "name^50 description^10 ")
            set("q.op", "OR")
            set("defType", "edismax")
        }
        val results = solrClient.query(coreName, solrQuery).results
        return if (results.numFound > 0) {
            results.map { it.toSolrResultBean() }
        } else {
            emptyList()
        }
    }

    fun fuzzyQuery(query: String): String {
        val replace = query.replace(" ", "~ ")
        return "$replace~"
    }

    private fun push(docs: List<DocumentIndexBean>) {
        if (docs.isNotEmpty()) {
            solrClient.addBeans(coreName, docs)
            solrClient.commit(coreName)
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(DocumentIndexService::class.java)
    }
}
