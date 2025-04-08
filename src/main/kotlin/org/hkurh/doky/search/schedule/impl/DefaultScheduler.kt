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

    private val log = KotlinLogging.logger {}

    @Scheduled(cron = "\${scheduler.documents.index.full}")
    override fun fullIndex() {
        log.info { "Starting full index" }
        indexService.fullIndex()
        log.info { "Full index complete" }
    }
}
