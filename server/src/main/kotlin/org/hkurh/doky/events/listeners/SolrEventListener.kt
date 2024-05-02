package org.hkurh.doky.events.listeners

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.events.DocumentUpdatedEvent
import org.hkurh.doky.search.DocumentIndexService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SolrEventListener(
    private val documentIndexService: DocumentIndexService
) {

    @EventListener
    fun updateDocumentInfo(event: DocumentUpdatedEvent) {
        LOG.debug { "Process updated document event for [${event.document.id}]" }
        documentIndexService.updateDocumentInfo(event.document)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
