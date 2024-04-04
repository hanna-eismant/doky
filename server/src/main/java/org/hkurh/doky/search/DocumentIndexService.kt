package org.hkurh.doky.search

import org.apache.solr.client.solrj.SolrClient
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

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
            .apply {
                solrClient.addBeans(coreName, this)
                solrClient.commit(coreName)
            }
    }
}
