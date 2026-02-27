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

package org.hkurh.doky.search.index.impl

import com.algolia.api.SearchClient
import com.algolia.model.search.BatchResponse
import com.algolia.model.search.SaveObjectResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.DocumentAccessService
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.search.DocumentIndexData
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.util.*

@DisplayName("AlgoliaIndexService unit test")
class AlgoliaIndexServiceTest : DokyUnitTest {

    private val documentEntityRepository: DocumentEntityRepository = mock()
    private val documentAccessService: DocumentAccessService = mock()
    private val searchClient: SearchClient = mock()
    private val indexName: String = "documents"

    private val indexService =
        AlgoliaIndexService(documentEntityRepository, documentAccessService, searchClient, indexName)


    @Test
    @DisplayName("Should clear before do full index")
    fun shouldClearAllBeforeFullIndex() {
        // when
        indexService.fullIndex()

        // then
        val searchClientInOrder = inOrder(searchClient)
        searchClientInOrder.verify(searchClient).clearObjects(indexName)
        searchClientInOrder.verify(searchClient).saveObjects(eq(indexName), any<List<DocumentIndexData>>())
    }

    @Test
    @DisplayName("Should populate allowed users when do full index")
    fun shouldPopulateAllowedUsers_whenFullIndex() {
        // given
        val documentEntities = createDocumentEntities()
        whenever(documentEntityRepository.findAll()).thenReturn(documentEntities)

        // when
        indexService.fullIndex()

        // then
        val captor = argumentCaptor<DocumentIndexData>()
        verify(documentAccessService, times(2))
            .populateAllowedUsers(captor.capture())
        val capturedArgs = captor.allValues
        assert(capturedArgs.size == 2)
        assert(capturedArgs[0].objectID == "1")
        assert(capturedArgs[1].objectID == "2")
    }

    @Test
    @DisplayName("Should send all documents to index")
    fun shouldSendAllDocumentsToIndex() {
        // given
        val documentEntities = createDocumentEntities()
        whenever(documentEntityRepository.findAll()).thenReturn(documentEntities)
        whenever(documentAccessService.populateAllowedUsers(any())).thenAnswer { invocation ->
            val arg = invocation.arguments[0] as DocumentIndexData
            arg.allowedUsers = mutableListOf("user-${arg.objectID}")
            arg
        }

        // when
        indexService.fullIndex()

        // then
        val captor = argumentCaptor<List<DocumentIndexData>>()
        verify(searchClient).saveObjects(eq(indexName), captor.capture())
        val savedDocs = captor.firstValue
        assertSoftly {
            assertThat(savedDocs).hasSize(2)
            assertThat(savedDocs[0].allowedUsers).containsExactly("user-1")
            assertThat(savedDocs[0].objectID).isEqualTo("1")
            assertThat(savedDocs[1].allowedUsers).containsExactly("user-2")
            assertThat(savedDocs[1].objectID).isEqualTo("2")
        }
    }

    @Test
    @DisplayName("Should wait until full index finished")
    fun shouldWaitUntilFullIndexFinished() {
        // given
        whenever(searchClient.saveObjects(eq(indexName), any<List<DocumentIndexData>>()))
            .thenReturn(createBatchResponseOk())

        // when
        indexService.fullIndex()

        // then
        verify(searchClient).waitForTask(eq(indexName), eq(1))
    }

    @Test
    @DisplayName("Should do nothing when no document for update")
    fun shouldDoNothing_whenNoDocumentForUpdate() {
        // when
        indexService.updateIndex(1)

        // then
        verifyNoInteractions(documentAccessService, searchClient)
    }

    @Test
    @DisplayName("Should populate allowed users when update document in index")
    fun shouldPopulateAllowedUsers_whenUpdateDocument() {
        // given
        val docId = 1L
        val doc = DocumentEntity().apply { id = docId }
        whenever(documentEntityRepository.findById(docId)).thenReturn(Optional.of(doc))
        whenever(searchClient.saveObject(eq(indexName), any<DocumentIndexData>())).thenReturn(createSaveResponseOk())

        // when
        indexService.updateIndex(docId)

        // then
        val captor = argumentCaptor<DocumentIndexData>()
        verify(documentAccessService).populateAllowedUsers(captor.capture())
        val capturedArgs = captor.allValues
        assert(capturedArgs[0].objectID == "1")
    }

    @Test
    @DisplayName("Should send document to index")
    fun shouldSendDocumentToIndex() {
        // given
        val docId = 1L
        val doc = DocumentEntity().apply { id = docId }
        whenever(documentEntityRepository.findById(docId)).thenReturn(Optional.of(doc))
        whenever(documentAccessService.populateAllowedUsers(any())).thenAnswer { invocation ->
            val arg = invocation.arguments[0] as DocumentIndexData
            arg.allowedUsers = mutableListOf("user-${arg.objectID}")
            arg
        }
        whenever(searchClient.saveObject(eq(indexName), any<DocumentIndexData>())).thenReturn(createSaveResponseOk())

        // when
        indexService.updateIndex(docId)

        // then
        val captor = argumentCaptor<DocumentIndexData>()
        verify(searchClient).saveObject(eq(indexName), captor.capture())
        val savedDoc = captor.firstValue
        assertSoftly {
            assertThat(savedDoc.allowedUsers).containsExactly("user-1")
            assertThat(savedDoc.objectID).isEqualTo("1")
        }
    }

    @Test
    @DisplayName("Should wait until document update finished")
    fun shouldWaitUntilDocumentUpdateFinished() {
        // given
        val docId = 1L
        val doc = DocumentEntity().apply { id = docId }
        whenever(documentEntityRepository.findById(docId)).thenReturn(Optional.of(doc))
        whenever(searchClient.saveObject(eq(indexName), any<DocumentIndexData>())).thenReturn(createSaveResponseOk())

        // when
        indexService.updateIndex(docId)

        // then
        verify(searchClient).waitForTask(eq(indexName), eq(1))
    }

    private fun createDocumentEntities(): List<DocumentEntity> {
        val doc1 = DocumentEntity().apply { id = 1 }
        val doc2 = DocumentEntity().apply { id = 2 }

        return listOf(doc1, doc2)
    }

    private fun createBatchResponseOk(): List<BatchResponse> {
        val response = BatchResponse().apply {
            taskID = 1
        }
        return listOf(response)
    }

    private fun createSaveResponseOk(): SaveObjectResponse {
        return SaveObjectResponse().apply {
            taskID = 1
        }
    }
}
