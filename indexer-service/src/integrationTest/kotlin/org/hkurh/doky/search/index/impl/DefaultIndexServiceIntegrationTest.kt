package org.hkurh.doky.search.index.impl

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import io.restassured.path.json.JsonPath
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.hkurh.doky.DokyIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.jdbc.Sql

@DisplayName("DefaultIndexServiceIntegration integration tests")
class DefaultIndexServiceIntegrationTest : DokyIntegrationTest() {

    val azureSearchApiVersion = "2024-07-01"
    val jsonResponses = "wiremock/DefaultIndexServiceIntegrationTest"

    @Value("\${azure.search.index-name}")
    lateinit var indexName: String

    @Autowired
    lateinit var indexService: DefaultIndexService

    @DisplayName("Should delete all documents and re-index all documents")
    @Test
    @Sql(
        scripts = ["classpath:sql/DefaultIndexServiceIntegrationTest/setup.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun shouldDeleteAllDocumentsAndReIndexAllDocuments() {
        // given
        createStub(
            path = "/indexes('$indexName')/docs/search.post.search",
            responseFile = "$jsonResponses/search-all.json"
        )
        createStub(path = "/indexes('$indexName')/docs/search.index", responseFile = "$jsonResponses/delete-all.json")

        // when
        indexService.fullIndex()

        // then
        val requests =
            WireMock.findAll(WireMock.postRequestedFor(urlPathEqualTo("/indexes('$indexName')/docs/search.index")))
                .map { it.bodyAsString }
        assertAll(
            "Verify captured requests",
            {
                assertThat(
                    JsonPath.from(requests[0]).getString("value[0]['@search.action']"),
                    allOf(notNullValue(), `is`("delete"))
                )
            },
            {
                assertThat(
                    JsonPath.from(requests[0]).getString("value[0]['name']"),
                    allOf(notNullValue(), `is`("Test_To_Delete"))
                )
            },
            {
                assertThat(
                    JsonPath.from(requests[1]).getString("value[0]['@search.action']"),
                    allOf(notNullValue(), `is`("upload"))
                )
            },
            {
                assertThat(
                    JsonPath.from(requests[1]).getString("value[0]['name']"),
                    allOf(notNullValue(), `is`("Test_1"))
                )
            }
        )
    }

    private fun createStub(path: String, responseFile: String) {
        WireMock.stubFor(
            post(urlPathEqualTo(path))
                .withQueryParam("api-version", equalTo(azureSearchApiVersion))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(loadJsonResponse(responseFile))
                )
        )
    }

    private fun loadJsonResponse(filePath: String): String {
        return this::class.java.classLoader.getResource(filePath).readText()
    }
}
