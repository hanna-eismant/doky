package org.hkurh.doky.authorization

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.users.UserFacade
import org.hkurh.doky.users.api.UserDto
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@DisplayName("AuthorizationUserController unit test")
class AuthorizationUserControllerTest : DokyUnitTest {


    private val userFacade: UserFacade = mock()
    private var controller = AuthorizationUserController(userFacade)

    @Test
    @DisplayName("Should generate token when login")
    fun shouldGenerateToken_whenLogin() {
        // given
        val userUid = "test@example.com"
        val userPassword = "password123"
        val authenticationRequest = AuthenticationRequest().apply {
            uid = userUid
            password = userPassword
        }
        whenever(userFacade.checkCredentials(userUid, userPassword)).thenReturn(
            UserDto().apply {
                uid = userUid
                roles = mutableSetOf("ROLE_USER")
            }
        )

        // when
        val response = controller.login(authenticationRequest)

        // then
        response.body!!.apply {
            assertFalse(token.isBlank())
        }
    }
}
