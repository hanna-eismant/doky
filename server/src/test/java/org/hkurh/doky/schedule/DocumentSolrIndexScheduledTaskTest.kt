package org.hkurh.doky.schedule

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.schedule.db.ScheduledTaskEntity
import org.hkurh.doky.schedule.db.ScheduledTaskEntityRepository
import org.hkurh.doky.search.DocumentIndexService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@DisplayName("DocumentSolrIndexScheduledTask unit test")
class DocumentSolrIndexScheduledTaskTest : DokyUnitTest {

    private val documentIndexService: DocumentIndexService = mock()
    private val scheduledTaskEntityRepository: ScheduledTaskEntityRepository = mock()
    private var documentSolrIndexScheduledTask =
        DocumentSolrIndexScheduledTask(documentIndexService, scheduledTaskEntityRepository)

    private val taskName = "solr-index-documents"
    private val scheduledTask = ScheduledTaskEntity()
    private val runDate = Date(0)

    @BeforeEach
    fun setUp() {
        scheduledTask.apply {
            name = taskName
            lastRunDate = runDate
        }
    }

    @Test
    @DisplayName("Should update last run date when do full index")
    fun shouldUpdateLastRunDate_whenFullIndex() {
        // given
        whenever(scheduledTaskEntityRepository.findByName(taskName)).thenReturn(scheduledTask)

        // when
        documentSolrIndexScheduledTask.runFullIndex()

        // then
        verify(documentIndexService).fullIndex()
        verify(scheduledTaskEntityRepository).save(check {
            assertTrue(it.name == taskName)
            assertTrue(it.lastRunDate!!.after(runDate))
        })
    }

    @Test
    @DisplayName("Should update last run date when no task info in database")
    fun shouldUpdateLastRunDate_whenNoTaskInDatabase() {
        // given
        whenever(scheduledTaskEntityRepository.findByName(taskName)).thenReturn(null)

        // when
        documentSolrIndexScheduledTask.runFullIndex()

        // then
        verify(documentIndexService).fullIndex()
        verify(scheduledTaskEntityRepository).save(check {
            assertTrue(it.name == taskName)
            assertTrue(it.lastRunDate != null)
        })
    }

    @Test
    @DisplayName("Should update last run date when do update index")
    fun shouldUpdateLastRunDate_whenUpdateIndex() {
        // given
        whenever(scheduledTaskEntityRepository.findByName(taskName)).thenReturn(scheduledTask)

        // when
        documentSolrIndexScheduledTask.runUpdateIndex()

        // then
        verify(documentIndexService).updateIndex(runDate)
        verify(scheduledTaskEntityRepository).save(check {
            assertTrue(it.name == taskName)
            assertTrue(it.lastRunDate!!.after(runDate))
        })
    }
}
