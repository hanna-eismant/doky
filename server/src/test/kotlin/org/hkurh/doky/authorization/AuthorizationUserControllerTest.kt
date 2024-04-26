package org.hkurh.doky.authorization

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.users.UserFacade
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock


@DisplayName("AuthorizationUserController unit test")
class AuthorizationUserControllerTest : DokyUnitTest {


    private val userFacade: UserFacade = mock()
    private var controller = AuthorizationUserController(userFacade)

    @Test
    @DisplayName("Should generate token when login")
    fun shouldGenerateToken_whenLogin() {
        // given
        val authenticationRequest = AuthenticationRequest().apply {
            uid = "test@example.com"
            password = "password123"
        }

        // when
        val response = controller.login(authenticationRequest)

        // then
        response.body!!.apply {
            assertFalse(token.isBlank())
        }
    }
}