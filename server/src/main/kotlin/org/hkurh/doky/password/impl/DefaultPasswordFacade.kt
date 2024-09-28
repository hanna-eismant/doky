package org.hkurh.doky.password.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationService
import org.hkurh.doky.password.PasswordFacade
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component


@Component
class DefaultPasswordFacade(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService,
    private val kafkaEmailNotificationService: KafkaEmailNotificationService
) : PasswordFacade {

    override fun reset(email: String) {
        if (!userService.exists(email)) {
            LOG.debug { "Requested reset password token for non existing user" }
            return
        }

        val user = userService.findUserByUid(email)
        resetPasswordService.generateAndSaveResetToken(user!!)
        LOG.debug { "Generate reset password token for user [${user.id}]" }
        kafkaEmailNotificationService.sendNotification(user.id, EmailType.RESET_PASSWORD)
    }


    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
