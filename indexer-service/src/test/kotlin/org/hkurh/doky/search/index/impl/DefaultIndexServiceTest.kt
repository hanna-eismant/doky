package org.hkurh.doky.search.index.impl

import com.azure.search.documents.SearchClient
import com.azure.search.documents.util.SearchPagedIterable
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("DefaultIndexService unit test")
class DefaultIndexServiceTest : DokyUnitTest {

    private var documentEntityRepository: DocumentEntityRepository = mock()
    private var searchClient: SearchClient = mock()

    private val searchResults: SearchPagedIterable = mock()

    private var documentFacade = DefaultIndexService(documentEntityRepository, searchClient)


    @Disabled("Not yet implemented")
    @Test
    @DisplayName("Full index")
    fun shouldCleanupIndex() {
        // given
//        val searchResults = createSearchResponse()
        whenever(searchClient.search("*")).thenReturn(searchResults)

        // when
        documentFacade.fullIndex()

        // then
    }

//    private fun createSearchResponse() : SearchPagedIterable {
//        val mockResult1 = SearchResult(1.0)
//        val mockResult2 = SearchResult(0.5)
//
//        mockResult1.additionalProperties["id"] = "doc1"
//        mockResult1.additionalProperties["name"] = "Document 1 Title"
//
//        mockResult2.additionalProperties["id"] = "doc2"
//        mockResult2.additionalProperties["name"] = "Document 2 Title"
//
//        // Combine them into a list
//        val mockResults = listOf(mockResult1, mockResult2)
//
//        // Create a mocked SearchPagedResponse
//        val searchPagedResponse = SearchPagedResponse()
//        searchPagedResponse.setValue(mockResults)
//
//        // Create a Supplier to provide the SearchPagedResponse
//        val responseSupplier = Supplier { searchPagedResponse }
//
//        // Return the SearchPagedIterable
//        return SearchPagedIterable(responseSupplier)
//    }

}
