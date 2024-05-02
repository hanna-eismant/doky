package org.hkurh.doky.search

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams
import org.apache.solr.common.params.ModifiableSolrParams
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.search.impl.DefaultDocumentIndexService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.jdbc.Sql


@DisplayName("DefaultDocumentIndexService integration test")
class DefaultDocumentIndexServiceIntegrationTest : DokyIntegrationTest() {
    val log = KotlinLogging.logger { }

    @Value("\${doky.search.solr.core.documents:documents-test}")
    lateinit var coreName: String

    @Autowired
    lateinit var documentIndexer: DefaultDocumentIndexService

    @Autowired
    lateinit var solrClient: SolrClient

    @BeforeEach
    fun setUp() {
        solrClient.deleteByQuery(coreName, "*:*")
    }

    @Test
    @Sql(
        scripts = ["classpath:sql/DocumentIndexServiceIntegrationTest/setup.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = ["classpath:sql/DocumentIndexServiceIntegrationTest/cleanup.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Should add to index all documents")
    fun shouldAddToIndexAllDocuments() {
        // when
        documentIndexer.fullIndex()

        // then
        val solrParams = ModifiableSolrParams()
        solrParams.add(CommonParams.Q, "*:*")
        val results = solrClient.query(coreName, solrParams).results
        logSearchResult(results)
        assertEquals(4, results.size)
    }

    private fun logSearchResult(results: SolrDocumentList) {
        log.debug { "Documents found by solr" }
        results.forEach {
            log.debug { it["name"] }
        }
    }
}
