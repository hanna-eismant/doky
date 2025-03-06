/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky

import com.azure.core.credential.AzureKeyCredential
import com.azure.search.documents.SearchClient
import com.azure.search.documents.SearchClientBuilder
import org.hkurh.doky.search.LoggingPolicy
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
            .addPolicy(LoggingPolicy())
            .buildClient()
    }
}
