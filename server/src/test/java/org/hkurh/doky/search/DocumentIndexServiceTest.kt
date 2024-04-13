package org.hkurh.doky.search

import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocumentList
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.solr.DocumentIndexBean
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@DisplayName("DocumentIndexService unit test")
class DocumentIndexServiceTest : DokyUnitTest {

    @InjectMocks
    @Spy
    lateinit var documentIndexService: DocumentIndexService
    private var documentEntityRepository: DocumentEntityRepository = mock()
    private var solrClient: SolrClient = mock()

    @BeforeEach
    fun setUp() {
        documentIndexService.coreName = "solr-core"
    }

    @Test
    @DisplayName("Should add document to index when do full index")
    fun shouldAddDocumentToIndex_whenFullIndex() {
        // given
        val document = DocumentEntity().apply {
            id = 1
            name = "test"
        }
        whenever(documentEntityRepository.findAll()).thenReturn(listOf(document))

        // when
        documentIndexService.fullIndex()

        // then
        verify(solrClient).addBeans(any<String>(), any<List<DocumentIndexBean>>())
        verify(solrClient).commit(any<String>())
    }

    @Test
    @DisplayName("Should add document to index when do update index")
    fun shouldAddDocumentToIndex_whenUpdateIndex() {
        // given
        val document = DocumentEntity().apply {
            id = 1
            name = "test"
        }
        val date = Date()
        whenever(documentEntityRepository.findLatestModified(date)).thenReturn(listOf(document))

        // when
        documentIndexService.updateIndex(date)

        // then
        verify(solrClient).addBeans(any<String>(), any<List<DocumentIndexBean>>())
        verify(solrClient).commit(any<String>())
    }

    @Test
    @DisplayName("Should set solr search parameters")
    fun shouldSetSolrParameters() {
        // given
        val query = "some text"
        val response = mock<QueryResponse> {
            on { results } doReturn SolrDocumentList()
        }
        whenever(solrClient.query(any<String>(), any())).thenReturn(response)

        // when
        documentIndexService.search(query)

        // then
        verify(solrClient).query(any<String>(), check {
            assertEquals("edismax", it.get("defType"))
            assertNotNull(it.get("q"))
            assertNotNull(it.get("qf"))
            assertNotNull(it.get("pf"))
        })
    }

    @Test
    @DisplayName("Should adjust query to make it fuzzy")
    fun shouldAdjustQueryToFuzzy() {
        // given
        val query = "some text"
        val response = mock<QueryResponse> {
            on { results } doReturn SolrDocumentList()
        }
        whenever(solrClient.query(any<String>(), any())).thenReturn(response)

        // when
        documentIndexService.search(query)

        // then
        verify(solrClient).query(any<String>(), check {
            assertEquals("some~ text~", it.get("q"))
        })
    }
}
