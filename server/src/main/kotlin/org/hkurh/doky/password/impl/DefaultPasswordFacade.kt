package org.hkurh.doky.password.impl

import org.hkurh.doky.email.EmailService
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.password.PasswordFacade
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DefaultPasswordFacade(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService,
    private val emailService: EmailService
) : PasswordFacade {

    override fun reset(email: String) {
        if (!userService.exists(email)) throw DokyNotFoundException("User does not exist")

        val user = userService.findUserByUid(email)
        val token = resetPasswordService.generateAndSaveResetToken(user!!)
        LOG.debug("Generate reset password token for user ${user.id}")
        try {
            emailService.sendRestorePasswordEmail(user, token)
        } catch (e: Exception) {
            LOG.error("Error sending reset password email for user ${user.id}", e)
        }
    }


    companion object {
        private val LOG = LoggerFactory.getLogger(DefaultPasswordFacade::class.java)
    }
}