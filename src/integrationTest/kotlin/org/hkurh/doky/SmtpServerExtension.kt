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

package org.hkurh.doky

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor

class SmtpServerExtension : BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback,
    TestInstancePostProcessor {

    private val greenMail = GreenMail(ServerSetup(2525, null, ServerSetup.PROTOCOL_SMTP))

    override fun beforeAll(context: ExtensionContext?) {
        greenMail.apply {
            withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication())
            start()
        }
    }

    override fun afterAll(context: ExtensionContext?) {
        greenMail.stop()
    }

    override fun beforeTestExecution(context: ExtensionContext?) {
        greenMail.purgeEmailFromAllMailboxes()
    }

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        testInstance?.javaClass?.getMethod("setSmtpServer", GreenMail::class.java)?.invoke(testInstance, greenMail)
    }
}
