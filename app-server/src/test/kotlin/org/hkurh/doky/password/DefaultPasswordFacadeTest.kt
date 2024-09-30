package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.password.impl.DefaultPasswordFacade
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@DisplayName("DefaultPasswordFacade unit test")
class DefaultPasswordFacadeTest : DokyUnitTest {

    private val userService: UserService = mock()
    private val resetPasswordService: ResetPasswordService = mock()
    private val kafkaEmailNotificationProducerService: KafkaEmailNotificationProducerService = mock()
    private val passwordFacade =
        DefaultPasswordFacade(userService, resetPasswordService, kafkaEmailNotificationProducerService)

    private val userEmail = "test@example.com"
    private val generatedToken = "token"
    private val userEntity = UserEntity().apply {
        id = 14
        uid = userEmail
    }

    @Test
    @DisplayName("Should generate reset password token when user exists")
    fun shouldGenerateToken_whenUserExists() {
        // given
        whenever(userService.exists(userEmail)).thenReturn(true)
        whenever(userService.findUserByUid(userEmail)).thenReturn(userEntity)
        whenever(resetPasswordService.generateAndSaveResetToken(userEntity)).thenReturn(generatedToken)

        // when
        passwordFacade.reset(userEmail)

        // then
        verify(resetPasswordService, times(1)).generateAndSaveResetToken(userEntity)
    }

    @Test
    @DisplayName("Should send email with reset password token when user exists")
    fun shouldSendEmailWithResetPassword_whenUserExists() {
        // given
        whenever(userService.exists(userEmail)).thenReturn(true)
        whenever(userService.findUserByUid(userEmail)).thenReturn(userEntity)
        whenever(resetPasswordService.generateAndSaveResetToken(userEntity)).thenReturn(generatedToken)

        // when
        passwordFacade.reset(userEmail)

        // then
        verify(kafkaEmailNotificationProducerService, times(1)).sendNotification(
            userEntity.id,
            EmailType.RESET_PASSWORD
        )
    }

    @Test
    @DisplayName("Should throw exception when user doesn't exist")
    fun shouldDoNothing_whenUserDoesNotExist() {
        // given
        whenever(userService.exists(userEmail)).thenReturn(false)

        // when - then
        passwordFacade.reset(userEmail)

        // then
        verifyNoMoreInteractions(resetPasswordService)
        verifyNoMoreInteractions(kafkaEmailNotificationProducerService)
    }
}
