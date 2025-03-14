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

package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.impl.DefaultPasswordFacade
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder

@DisplayName("DefaultPasswordFacade unit test")
class DefaultPasswordFacadeTest : DokyUnitTest {

    private val userService: UserService = mock()
    private val resetPasswordService: ResetPasswordService = mock()
    private val kafkaEmailNotificationProducerService: KafkaEmailNotificationProducerService = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private val passwordFacade = DefaultPasswordFacade(
        userService = userService,
        resetPasswordService = resetPasswordService,
        kafkaEmailNotificationProducerService = kafkaEmailNotificationProducerService,
        passwordEncoder = passwordEncoder
    )

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

    @Test
    @DisplayName("Should update password for user when token is valid")
    fun shouldUpdatePassword_whenTokenIsValid() {
        // given
        val initialPassword = "<PASSWORD>"
        val newPassword = "New-Passw0rd"
        val encodedPassword = "Enc0ded-PassW0rd"
        val user = UserEntity().apply {
            uid = "user@mail.com"
            password = initialPassword
        }
        val token = "token"
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.user = user
        }
        whenever(resetPasswordService.checkToken(token)).thenReturn(resetPasswordTokenEntity)
        whenever(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword)

        // when
        passwordFacade.update(newPassword, token)

        // then
        verify(userService, times(1)).updateUser(argThat<UserEntity> { arg ->
            arg.uid == user.uid && arg.password == encodedPassword
        })
    }

    @Test
    @DisplayName("Should not update password for user when token is invalid")
    fun shouldNotUpdatePassword_whenTokenIsInvalid() {
        // given
        val newPassword = "New-Passw0rd"
        val token = "token"
        whenever(resetPasswordService.checkToken(token)).thenThrow(DokyRegistrationException("Invalid token"))

        // when
        assertThrows<DokyRegistrationException> {
            passwordFacade.update(newPassword, token)
        }

        // then
        verify(userService, times(0)).updateUser(any())
    }

    @Test
    @DisplayName("Should remove reset password entity after updating password")
    fun shouldRemoveResetPasswordEntity_whenSuccessfullyUpdatePassword() {
        // given
        val initialPassword = "<PASSWORD>"
        val newPassword = "New-Passw0rd"
        val encodedPassword = "Enc0ded-PassW0rd"
        val user = UserEntity().apply {
            uid = "user@mail.com"
            password = initialPassword
        }
        val token = "token"
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.user = user
        }
        whenever(resetPasswordService.checkToken(token)).thenReturn(resetPasswordTokenEntity)
        whenever(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword)

        // when
        passwordFacade.update(newPassword, token)

        // then
        verify(resetPasswordService, times(1)).delete(resetPasswordTokenEntity)
    }
}
