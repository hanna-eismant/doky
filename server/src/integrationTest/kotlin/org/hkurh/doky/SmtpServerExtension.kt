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
