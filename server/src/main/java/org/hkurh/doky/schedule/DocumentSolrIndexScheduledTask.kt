package org.hkurh.doky.schedule

import org.apache.commons.logging.LogFactory
import org.hkurh.doky.schedule.db.ScheduledTaskEntityRepository
import org.hkurh.doky.search.DocumentIndexService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class DocumentSolrIndexScheduledTask(
    val documentIndexService: DocumentIndexService,
    val scheduledTaskEntityRepository: ScheduledTaskEntityRepository
) {

    private val taskName = "solr-index-documents"

    @Scheduled(cron = "0 */10 * * * *")
//    @Scheduled(cron = "0 0 3 * * SUN")
    fun runFullIndex() {
        LOG.info("Start full solr indexing for Documents")

//        documentIndexService.fullIndex()

        scheduledTaskEntityRepository.findByName(taskName)
            .let {
                it.modifiedDate = Date()
                scheduledTaskEntityRepository.save(it)
            }

        LOG.info("Finish full solr indexing for Documents")
    }

//    @Scheduled(cron = "0 */2 * * * *")
//    @Scheduled(cron = "0 0 3 * * MON-SAT")
    fun runUpdateIndex() {
        val taskName = "solr-update-index-documents"
        LOG.info("RUN UPDATE INDEX")
    }

    companion object {
        private val LOG = LogFactory.getLog(DocumentSolrIndexScheduledTask::class.java)
    }

}
