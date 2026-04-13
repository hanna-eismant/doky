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

package org.hkurh.doky.search.impl

import com.algolia.api.SearchClient
import com.algolia.model.search.SearchParamsObject
import com.algolia.model.search.SearchResponse
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.api.Page
import org.hkurh.doky.documents.api.Sort
import org.hkurh.doky.documents.api.SortDirection
import org.hkurh.doky.search.DocumentResultData
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.stream.Stream

@DisplayName("AlgoliaDocumentSearchService unit test")
class AlgoliaDocumentSearchServiceTest : DokyUnitTest {

    private val userService: UserService = mock()
    private val searchClient: SearchClient = mock()
    private val indexName: String = "documents"

    private val algoliaDocumentSearchService = AlgoliaDocumentSearchService(userService, searchClient, indexName)

    @Test
    @DisplayName("Should perform search with provided parameters")
    fun shouldPerformSearchWithProvidedParameters() {
        // given
        val expectedPageNumber = 3
        val expectedPageSize = 13
        val page = Page(expectedPageNumber, expectedPageSize)
        val sort = Sort("createdDate", SortDirection.DESC)
        whenever(userService.getCurrentUser()).thenReturn(createUser())
        val searchResponse = createSearchResult()
        whenever(searchClient.searchSingleIndex(any(), any(), eq(DocumentResultData::class.java)))
            .thenReturn(searchResponse)

        // when
        algoliaDocumentSearchService.search("", page, sort)

        // then
        argumentCaptor<SearchParamsObject> {
            verify(searchClient).searchSingleIndex(
                eq("documents_createdDateTs_desc"),
                capture(),
                eq(DocumentResultData::class.java)
            )
            val actualParams = firstValue
            assertEquals(expectedPageNumber, actualParams.page)
            assertEquals(expectedPageSize, actualParams.hitsPerPage)
        }
    }

    @Test
    @DisplayName("Should apply default sort parameters when no sort provided")
    fun shouldApplyDefaultSortParameters_whenNoSortProvided() {
        // given
        val page = Page(2, 10)
        val sort = Sort(null, null)
        whenever(userService.getCurrentUser()).thenReturn(createUser())
        val searchResponse = SearchResponse<DocumentResultData>()
        whenever(searchClient.searchSingleIndex(any(), any(), eq(DocumentResultData::class.java)))
            .thenReturn(searchResponse)

        // when
        algoliaDocumentSearchService.search("", page, sort)

        // then
        verify(searchClient).searchSingleIndex(eq("documents"), any(), eq(DocumentResultData::class.java))
    }

    @ParameterizedTest
    @MethodSource("provideSortParameters")
    @DisplayName("Should apply sort parameters when sort provided")
    fun shouldApplySortParameters_whenSortProvided(
        sortField: String, sortDirection: SortDirection,
        expectedIndex: String
    ) {
        // given
        val page = Page(2, 10)
        val sort = Sort(sortField, sortDirection)
        whenever(userService.getCurrentUser()).thenReturn(createUser())
        val searchResponse = SearchResponse<DocumentResultData>()
        whenever(searchClient.searchSingleIndex(any(), any(), eq(DocumentResultData::class.java)))
            .thenReturn(searchResponse)

        // when
        algoliaDocumentSearchService.search("", page, sort)

        // then
        verify(searchClient).searchSingleIndex(eq(expectedIndex), any(), eq(DocumentResultData::class.java))
    }

    private fun createUser(): UserEntity {
        return UserEntity().apply {
            id = 1L
            name = "name"
            uid = "test@test.org"
        }
    }

    private fun createSearchResult(): SearchResponse<DocumentResultData> {
        return SearchResponse<DocumentResultData>().apply {
            hits = listOf()
            nbHits = 57
            page = 3
            nbPages = 16
        }
    }

    companion object {
        @JvmStatic
        private fun provideSortParameters(): Stream<Arguments> =
            Stream.of(
                Arguments.of("createdDate", SortDirection.ASC, "documents_createdDateTs_asc"),
                Arguments.of("createdDate", SortDirection.DESC, "documents_createdDateTs_desc"),
                Arguments.of("modifiedDate", SortDirection.ASC, "documents_modifiedDateTs_asc"),
                Arguments.of("modifiedDate", SortDirection.DESC, "documents_modifiedDateTs_desc"),
                Arguments.of("fileName", SortDirection.ASC, "documents_fileName_asc"),
                Arguments.of("fileName", SortDirection.DESC, "documents_fileName_desc"),
                Arguments.of("name", SortDirection.ASC, "documents_name_asc"),
                Arguments.of("name", SortDirection.DESC, "documents_name_desc"),
                Arguments.of("another", SortDirection.ASC, "documents"),
            )
    }
}
