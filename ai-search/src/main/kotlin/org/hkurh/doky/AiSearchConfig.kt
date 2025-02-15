package org.hkurh.doky

import com.azure.core.credential.AzureKeyCredential
import com.azure.search.documents.SearchClient
import com.azure.search.documents.SearchClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AiSearchConfig {

    @Value("\${azure.search.endpoint}")
    private var azureSearchEndpoint: String = ""

    @Value("\${azure.search.api-key}")
    private var azureSearchKey: String = ""

    @Value("\${azure.search.index-name}")
    private var azureSearchIndexName: String = ""

    @Bean
    fun searchClient(): SearchClient {
        return SearchClientBuilder()
            .endpoint(azureSearchEndpoint)
            .credential(AzureKeyCredential(azureSearchKey))
            .indexName(azureSearchIndexName)
//            .addPolicy(LoggingPolicy())
            .buildClient()
    }
}
