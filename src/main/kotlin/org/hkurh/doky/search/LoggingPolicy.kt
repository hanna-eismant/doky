/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.search

import com.azure.core.http.HttpPipelineCallContext
import com.azure.core.http.HttpPipelineNextPolicy
import com.azure.core.http.HttpResponse
import com.azure.core.http.policy.HttpPipelinePolicy
import io.github.oshai.kotlinlogging.KotlinLogging
import reactor.core.publisher.Mono

class LoggingPolicy : HttpPipelinePolicy {

    private val log = KotlinLogging.logger {}

    override fun process(context: HttpPipelineCallContext, next: HttpPipelineNextPolicy): Mono<HttpResponse> {
        val request = context.httpRequest
        log.debug {
            "Request: Method=${request.httpMethod}, URL=${request.url}, Body=${
                request.body
            }"
        }

        return next.process().doOnNext { response ->
            log.debug {
                "Response: Status=${response.statusCode}, Body=${
                    response.bodyAsString.block()
                }"
            }
        }
    }
}
