package org.hkurh.doky.kafka

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenRepository
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.util.*


@DisplayName("KafkaEmailNotificationConsumerService unit test")
class KafkaEmailNotificationConsumerServiceTest : DokyUnitTest {

    private val userEntityRepository: UserEntityRepository = mock()
    private val resetPasswordTokenRepository: ResetPasswordTokenRepository = mock()
    private val emailService: EmailService = mock()

    private val service =
        KafkaEmailNotificationConsumerService(userEntityRepository, resetPasswordTokenRepository, emailService)


    @Test
    @DisplayName("Should send registration email when user exists")
    fun shouldSendRegistrationEmail() {
        // given
        val existingUserId = 16L
        val user = createUser(existingUserId)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(Optional.ofNullable(user) as Optional<UserEntity?>)

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.REGISTRATION
        }

        // when
        service.listen(message)

        // then
        verify(emailService, times(1)).sendRegistrationConfirmationEmail(user)
    }

    @Test
    @DisplayName("Should send reset password email when user and reset token exist")
    fun shouldSendResetPasswordEmail() {
        // given
        val existingUserId = 14L
        val token = "<TOKEN>"
        val user = createUser(existingUserId)
        val resetToken = createResetToken(user, token)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(Optional.ofNullable(user) as Optional<UserEntity?>)
        whenever(resetPasswordTokenRepository.findByUser(user)).thenReturn(resetToken)

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.RESET_PASSWORD
        }

        // when
        service.listen(message)

        // then
        verify(emailService, times(1)).sendRestorePasswordEmail(user, token)
    }

    private fun createUser(id: Long): UserEntity {
        return UserEntity().apply {
            this.id = id
            uid = "test@mail.com"
        }
    }

    private fun createResetToken(user: UserEntity, token: String): ResetPasswordTokenEntity {
        return ResetPasswordTokenEntity().apply {
            this.user = user
            this.token = token
        }
    }
}
