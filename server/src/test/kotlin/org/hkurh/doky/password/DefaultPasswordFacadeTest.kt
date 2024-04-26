package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.password.impl.DefaultPasswordFacade
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@DisplayName("DefaultPasswordFacadeTest unit test")
class DefaultPasswordFacadeTest : DokyUnitTest {

    private val userService: UserService = mock()
    private val resetPasswordService: ResetPasswordService = mock()
    private val emailService: EmailService = mock()
    private var passwordFacade = DefaultPasswordFacade(userService, resetPasswordService, emailService)

    private val userEmail = "test@example.com"
    private val generatedToken = "token"
    private val userEntity = UserEntity().apply {
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
        verify(emailService, times(1)).sendRestorePasswordEmail(userEntity, generatedToken)
    }

    @Test
    @DisplayName("Should throw exception when user doesn't exist")
    fun shouldThrowException_whenUserDoesNotExist() {
        // given
        whenever(userService.exists(userEmail)).thenReturn(false)

        // when - then
        assertThrows<DokyNotFoundException> { passwordFacade.reset(userEmail) }

        // then
        verifyNoMoreInteractions(resetPasswordService)
        verifyNoMoreInteractions(emailService)
    }
}
