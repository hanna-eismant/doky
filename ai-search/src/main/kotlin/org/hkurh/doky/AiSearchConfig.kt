package org.hkurh.doky

import com.azure.core.credential.AzureKeyCredential
import com.azure.search.documents.indexes.SearchIndexClient
import com.azure.search.documents.indexes.SearchIndexClientBuilder
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.azure.AzureVectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AiSearchConfig {

    @Value("\${spring.ai.vectorstore.azure.url}")
    private var azureSearchEndpoint: String = ""

    @Value("\${spring.ai.vectorstore.azure.api-key}")
    private var azureSearchKey: String = ""

    @Value("\${spring.ai.vectorstore.azure.index-name}")
    private var azureSearchIndexName: String = ""

    @Bean
    fun searchIndexClient(): SearchIndexClient {
        return SearchIndexClientBuilder().endpoint(azureSearchEndpoint)
            .credential(AzureKeyCredential(azureSearchKey))
            .buildClient()
    }

    @Bean
    fun vectorStore(searchIndexClient: SearchIndexClient, embeddingModel: EmbeddingModel): VectorStore {
        return AzureVectorStore.builder(searchIndexClient, embeddingModel)
            .initializeSchema(true)
            .indexName(azureSearchIndexName)
            .build()
    }
}
