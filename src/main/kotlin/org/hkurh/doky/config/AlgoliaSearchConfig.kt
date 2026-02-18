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

import com.algolia.api.SearchClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for setting up the Algolia Search client in the application.
 *
 * This class is annotated with `@Configuration` to designate it as a source of Spring Boot bean definitions.
 * It retrieves Algolia configuration properties, such as the application ID and API key, from the application
 * configuration files using the `@Value` annotation.
 *
 * The primary bean provided is the `searchClient`, which facilitates communication with the Algolia Search API.
 * The client is constructed using the `SearchClient` implementation and the injected configuration values.
 */
@Configuration
class AlgoliaSearchConfig {

    @Value("\${algolia.search.app-id}")
    private lateinit var algoliaAppId: String

    @Value("\${algolia.search.api-key}")
    private lateinit var algoliaApiKey: String

    @Bean
    fun algoliaSearchClient() : SearchClient {
        return SearchClient(algoliaAppId, algoliaApiKey)
    }
}
