package org.hkurh.doky.users

import org.hkurh.doky.email.EmailService
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mail.MailSendException

@ExtendWith(MockitoExtension::class)
@DisplayName("UserService unit test")
class UserServiceTest {

    @Spy
    @InjectMocks
    private val userService: UserService? = null

    @Mock
    private val userEntityRepository: UserEntityRepository? = null

    @Mock
    private val emailService: EmailService? = null

    private val userUid = "user@mail.com"
    private val userPassword = "password"

    @Test
    @DisplayName("Should send registration email when user is successfully registered")
    fun shouldSendEmail_whenUserSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository?.save(any())).thenReturn(userEntity)

        // when
        assertDoesNotThrow { userService?.create(userUid, userPassword) }

        // then
        verify(emailService)?.sendRegistrationConfirmationEmail(userEntity)
    }

    @Test
    @DisplayName("Should not send registration email when user is not successfully registered")
    fun shouldNotSendEmail_whenUserNotSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository?.save(any())).thenThrow(RuntimeException())

        // when
        assertThrows<RuntimeException> { userService?.create(userUid, userPassword) }

        // then
        verify(emailService, never())?.sendRegistrationConfirmationEmail(userEntity)
    }

    @Test
    @DisplayName("Should not throw exception when sending email is not successfully")
    fun shouldNotThrowException_whenEmailSendNotSuccessfully() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository?.save(any())).thenReturn(userEntity)
        whenever(emailService?.sendRegistrationConfirmationEmail(userEntity)).thenThrow(MailSendException(""))

        // when - then
        assertDoesNotThrow { userService?.create(userUid, userPassword) }
    }

    private fun createUserEntity(): UserEntity {
        return UserEntity().apply {
            id = 1
            uid = userUid
            password = userPassword
        }
    }
}