package org.hkurh.doky.search.index.impl

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.hkurh.doky.DokyIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class DefaultIndexServiceIntegrationTest : DokyIntegrationTest() {

    @Value("\${azure.search.index-name}")
    lateinit var indexName: String
    val azureSearchApiVersion = "2024-07-01"

    @Autowired
    lateinit var indexService: DefaultIndexService

    @Test
    fun test() {
        WireMock.stubFor(
            WireMock.post(urlEqualTo("/indexes('$indexName')/docs/search.post.search?api-version=$azureSearchApiVersion"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                                "@odata.context": "https://doky.search.windows.net/indexes('documents-local')/${'$'}metadata#docs(*)",
                                "@odata.count": 1,
                                "value": [
                                    {
                                        "@search.score": 1,
                                        "id": "1",
                                        "name": "Test One",
                                        "description": "The first document created from new laptop !!!!",
                                        "fileName": "doky-test.txt",
                                        "createdDate": "2025-01-14T17:50:53.273Z",
                                        "modifiedDate": "2025-02-01T16:17:45.853Z",
                                        "createdBy": "1",
                                        "modifiedBy": "1"
                                    }
                                ]
                            }
                            """.trimIndent()
                        )
                )
        )

        indexService.fullIndex()
    }
}
