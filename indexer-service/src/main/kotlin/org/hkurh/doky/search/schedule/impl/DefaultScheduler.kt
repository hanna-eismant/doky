package org.hkurh.doky.search.schedule.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.search.index.impl.DefaultIndexService
import org.hkurh.doky.search.schedule.Scheduler
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:scheduler.properties")
class DefaultScheduler(
    private val indexService: DefaultIndexService
) : Scheduler {

    @Scheduled(cron = "\${scheduler.documents.index.full}")
    override fun fullIndex() {
        LOG.info { "Starting full index" }
        indexService.fullIndex()
        LOG.info { "Full index complete" }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
