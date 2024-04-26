package org.hkurh.doky.schedule

import org.apache.commons.logging.LogFactory
import org.hkurh.doky.schedule.db.ScheduledTaskEntity
import org.hkurh.doky.schedule.db.ScheduledTaskEntityRepository
import org.hkurh.doky.search.DocumentIndexService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

/**
 * This class represents a scheduled task for indexing documents into Solr.
 * It uses the [DocumentIndexService] to perform indexing operations and the [ScheduledTaskEntityRepository] to manage task information.
 *
 * @property documentIndexService The service for indexing documents.
 * @property scheduledTaskEntityRepository The repository for managing task information.
 */
@Component
class DocumentSolrIndexScheduledTask(
    val documentIndexService: DocumentIndexService,
    val scheduledTaskEntityRepository: ScheduledTaskEntityRepository
) {

    private val taskName = "solr-index-documents"

    /**
     * Schedules and runs a full index of documents into Solr.
     * This method is executed according to the cron expression "0 0 3 * * SUN".
     * After the indexing is complete, it updates the last run date of the task in the repository.
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    fun runFullIndex() {
        LOG.info("Full Index: Start solr indexing for Documents")

        documentIndexService.fullIndex()
        updateLastRunDate()

        LOG.info("Full Index: Finish solr indexing for Documents")
    }

    /**
     * Runs the update index task for indexing documents into Solr.
     * This method is executed according to the cron expression "0 0 3 * * MON-SAT".
     * It retrieves the last run date of the task from the [scheduledTaskEntityRepository] and
     * updates the index using the [documentIndexService].
     * After the indexing is complete, it updates the last run date of the task in the repository.
     *
     * @see [Scheduled]
     */
    @Scheduled(cron = "0 0 3 * * MON-SAT")
    fun runUpdateIndex() {
        LOG.info("Update Index: Start solr indexing for Documents")

        var runDate = Date(0)
        scheduledTaskEntityRepository.findByName(taskName)?.lastRunDate?.let { runDate = it }
        documentIndexService.updateIndex(runDate)
        updateLastRunDate()

        LOG.info("Update Index: Finish solr indexing for Documents")
    }

    private fun updateLastRunDate() {
        var taskEntity = scheduledTaskEntityRepository.findByName(taskName)
        if (taskEntity == null) {
            taskEntity = ScheduledTaskEntity()
            taskEntity.name = taskName
        }
        taskEntity.lastRunDate = Date()
        scheduledTaskEntityRepository.save(taskEntity)
    }

    companion object {
        private val LOG = LogFactory.getLog(DocumentSolrIndexScheduledTask::class.java)
    }
}
