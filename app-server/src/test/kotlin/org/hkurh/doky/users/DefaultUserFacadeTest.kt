package org.hkurh.doky.users

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.hkurh.doky.security.DokySecurityService
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.impl.DefaultUserFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder

@DisplayName("DefaultUserFacade unit test")
class DefaultUserFacadeTest : DokyUnitTest {
    private val userUid = "user"
    private val userPassword = "pass"
    private val userPasswordEncoded = "passEncoded"
    private val userEntity = UserEntity()

    private val userService: UserService = mock()
    private var dokySecurityService: DokySecurityService = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private var userFacade = DefaultUserFacade(userService, dokySecurityService, passwordEncoder)

    @BeforeEach
    fun setUp() {
        userEntity.password = userPasswordEncoded
        userEntity.uid = userUid
    }

    @Test
    @DisplayName("Should done without errors when user is valid when login")
    fun shouldSetToken_whenUserIsValid() {
        // given
        whenever(userService.exists(userUid)).thenReturn(true)
        whenever(userService.findUserByUid(userUid)).thenReturn(userEntity)
        whenever(passwordEncoder.matches(userPassword, userPasswordEncoded)).thenReturn(true)

        // when - then
        assertDoesNotThrow { userFacade.checkCredentials(userUid, userPassword) }
    }

    @Test
    @DisplayName("Should throw exception when user login is incorrect when login")
    fun shouldThrowException_whenUserIsIncorrect() {
        // given
        whenever(userService.exists(userUid)).thenReturn(false)

        // when - then
        assertThrows<DokyAuthenticationException> { userFacade.checkCredentials(userUid, userPassword) }
    }

    @Test
    @DisplayName("Should throw exception when user exists when register")
    fun shouldThrowException_whenExistingUserRegister() {
        // given
        whenever(userService.exists(userUid)).thenReturn(true)

        // when - then
        assertThrows<DokyRegistrationException> { userFacade.register(userUid, userPassword) }
    }

    @Test
    @DisplayName("Should register user when it doses not exists")
    fun shouldRegisterUser_whenUserDoesNotExists() {
        // given
        whenever(passwordEncoder.encode(userPassword)).thenReturn(userPasswordEncoded)
        whenever(userService.create(userUid, userPasswordEncoded)).thenReturn(userEntity)

        // when
        val registeredUserDto = userFacade.register(userUid, userPassword)

        // then
        assertNotNull(registeredUserDto, "Registered user cannot be null")
        assertEquals(userUid, registeredUserDto.uid, "User should be registered with provided uid")
    }
}
