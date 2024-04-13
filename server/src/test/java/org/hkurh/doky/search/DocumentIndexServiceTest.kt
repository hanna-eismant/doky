package org.hkurh.doky.search

import org.apache.solr.client.solrj.SolrClient
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.solr.DocumentIndexBean
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.kotlin.any
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

}
