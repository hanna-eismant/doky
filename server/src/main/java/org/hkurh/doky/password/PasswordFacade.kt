package org.hkurh.doky.password

import org.hkurh.doky.email.EmailService
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component

@Component
class PasswordFacade(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService,
    private val emailService: EmailService
) {

    fun reset(email: String) {
        if (!userService.exists(email)) throw DokyNotFoundException("User with email $email does not exist")

        val user = userService.findUserByUid(email)
        val token = resetPasswordService.generateAndSaveResetToken(user!!)
        emailService.sendRestorePasswordEmail(user, token)
    }
}
