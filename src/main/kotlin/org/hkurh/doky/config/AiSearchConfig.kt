/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.config

import com.azure.core.credential.AzureKeyCredential
import com.azure.search.documents.SearchClient
import com.azure.search.documents.SearchClientBuilder
import org.hkurh.doky.search.LoggingPolicy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for setting up the Azure Search client in the application.
 *
 * This class is annotated with `@Configuration` to mark it as a source of Spring-managed bean definitions.
 * It leverages `@Value` annotations to inject configuration properties for Azure Search including the endpoint,
 * API key, and index name from the application configuration files.
 *
 * The primary bean provided is the `searchClient`, which is built using the `SearchClientBuilder`.
 * This client is configured with an endpoint, credentials, an index name, and a custom logging policy.
 * The logging policy enables detailed logging of HTTP requests and responses for debugging and monitoring purposes.
 */
@Configuration
class AiSearchConfig {

    @Value("\${azure.search.endpoint}")
    private lateinit var azureSearchEndpoint: String

    @Value("\${azure.search.api-key}")
    private lateinit var azureSearchKey: String

    @Value("\${azure.search.index-name}")
    private lateinit var azureSearchIndexName: String

    @Bean
    fun searchClient(): SearchClient {
        return SearchClientBuilder()
            .endpoint(azureSearchEndpoint)
            .credential(AzureKeyCredential(azureSearchKey))
            .indexName(azureSearchIndexName)
            .addPolicy(LoggingPolicy())
            .buildClient()
    }
}
