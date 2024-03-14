package org.hkurh.doky.authorization

import org.hkurh.doky.users.UserFacade
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock


@ExtendWith(MockitoExtension::class)
@DisplayName("AuthorizationUserController unit test")
class AuthorizationUserControllerTest {

    @InjectMocks
    @Spy
    private val controller: AuthorizationUserController? = null
    private val userFacade: UserFacade = mock()

    @Test
    @DisplayName("Should generate token when login")
    fun shouldGenerateToken_whenLogin() {
        // given
        val authenticationRequest = AuthenticationRequest("user@mail.com", "password")

        // when
        val response = controller?.login(authenticationRequest)

        // then
        response!!.body!!.apply {
            assertFalse(token.isNullOrBlank())
        }
    }
}
