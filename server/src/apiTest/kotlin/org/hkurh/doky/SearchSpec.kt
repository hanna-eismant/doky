package org.hkurh.doky

import io.restassured.RestAssured.given
import org.apache.solr.client.solrj.SolrClient
import org.hkurh.doky.search.DocumentIndexService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


@DisplayName("Document Search API test")
class SearchSpec : RestSpec()  {
    val endpoint = "/documents/search"
    val documentNameProperty = "name"
    val documentName1 = "Test note 1"
    val documentName2 = "Test note 2"

    @Value("\${doky.search.solr.core.documents:documents-test}")
    lateinit var coreName: String
    @Autowired
    lateinit var documentIndexer: DocumentIndexService
    @Autowired
    lateinit var solrClient: SolrClient

    @BeforeEach
    fun setUp() {
        solrClient.deleteByQuery(coreName, "*:*")
        documentIndexer.fullIndex()
    }

    @Test
    @DisplayName("Should return only documents that allowed to user")
    @Sql(scripts = ["classpath:sql/SearchSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = ["classpath:sql/SearchSpec/cleanup.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    fun shouldReturnOnlyDocumentsForUser() {
        // given
        val requestSpec = prepareRequestSpecWithLogin().build()

        // when:
        val response = given(requestSpec).queryParams("q", "test note")  .get(endpoint)

        // then
        response.then().statusCode(HttpStatus.OK.value())
        val documents: List<Map<String, String>> = response.path(".")

        assertNotNull(documents.find { d -> (documentName1 == d[documentNameProperty]) })
        assertNull(documents.find { d -> (documentName2 == d[documentNameProperty]) })
    }

    @Test
    @DisplayName("Should return empty list if no results")
    fun shouldReturnEmptyList_whenNoResults() {
        // given
        val requestSpec = prepareRequestSpecWithLogin().build()

        // when:
        val response = given(requestSpec).queryParams("q", "therenoresults")  .get(endpoint)

        // then
        response.then().statusCode(HttpStatus.OK.value())
        val documents: List<Map<String, String>> = response.path(".")
        assertTrue(documents.isEmpty())
    }
}
