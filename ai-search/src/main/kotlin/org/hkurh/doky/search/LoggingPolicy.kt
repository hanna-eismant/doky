package org.hkurh.doky.search

import com.azure.core.http.HttpPipelineCallContext
import com.azure.core.http.HttpPipelineNextPolicy
import com.azure.core.http.HttpResponse
import com.azure.core.http.policy.HttpPipelinePolicy
import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono

class LoggingPolicy : HttpPipelinePolicy {

    override fun process(context: HttpPipelineCallContext, next: HttpPipelineNextPolicy): Mono<HttpResponse>? {
        val request = context.httpRequest
        LOG.debug {
            "Request: Method=${request.httpMethod}, URL=${request.url}, Headers=${request.headers}, Body=${
                request.body
            }"
        }

        return next.process().doOnNext { response ->
            LOG.debug {
                "Response: Status=${response.statusCode}, Headers=${response.headers}, Body=${
                    response.bodyAsString.block()
                }"
            }
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
