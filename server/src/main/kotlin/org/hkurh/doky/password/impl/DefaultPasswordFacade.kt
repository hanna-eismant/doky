package org.hkurh.doky.password.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.password.PasswordFacade
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Component

@Component
class DefaultPasswordFacade(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService,
    private val emailService: EmailService
) : PasswordFacade {

    override fun reset(email: String) {
        if (!userService.exists(email)) {
            LOG.debug { "Requested reset password token for non existing user" }
            return
        }

        val user = userService.findUserByUid(email)
        val resetToken = resetPasswordService.generateAndSaveResetToken(user!!)
        LOG.debug { "Generate reset password token for user [${user.id}]" }
        sendResetPasswordEmail(user, resetToken)
    }

    private fun sendResetPasswordEmail(user: UserEntity, resetToken: String) {
        try {
            emailService.sendRestorePasswordEmail(user, resetToken)
        } catch (e: Exception) {
            LOG.error(e) { "Error sending reset password email for user ${user.id}" }
        }
    }


    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
