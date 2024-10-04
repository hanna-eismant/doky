package org.hkurh.doky.password.impl

import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.jdbc.Sql
import java.sql.Types

@DisplayName("DefaultUserService integration test")
class DefaultPasswordFacadeIntegrationTest : DokyIntegrationTest() {

    @Autowired
    lateinit var passwordFacade: DefaultPasswordFacade

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    @DisplayName("Should update password and cleanup token when valid token")
    @Sql(
        scripts = ["classpath:sql/DefaultPasswordFacadeIntegrationTest/setup.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun shouldUpdatePassword_whenTokenValid() {
        // given
        val newPassword = "New-PassW0rd"
        val token = "valid-token"

        // when
        passwordFacade.update(newPassword, token)

        // then
        val updatesPassword = getUserPassword("hanna_test_2@example.com")
        assertTrue(passwordEncoder.matches(newPassword, updatesPassword))

        val usedToken = getTokenId(token)
        assertNull(usedToken)
    }

    @Test
    @DisplayName("Should throw error when invalid token")
    fun shouldThrowError_whenTokenNotExist() {
        // given
        val newPassword = "New-PassW0rd"
        val token = "incorrect-token"

        // when
        val exception = assertThrows<DokyInvalidTokenException> {
            passwordFacade.update(newPassword, token)
        }

        // then
        assertNotNull(exception)
        assertNotNull(exception.message)
        assertTrue(exception.message!!.contains("invalid"))
    }

    @Test
    @DisplayName("Should throw error when token expired")
    @Sql(
        scripts = ["classpath:sql/DefaultPasswordFacadeIntegrationTest/setup.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun shouldThrowError_whenTokenExpired() {
        // given
        val newPassword = "New-PassW0rd"
        val token = "expired-token"

        // when
        val exception = assertThrows<DokyInvalidTokenException> {
            passwordFacade.update(newPassword, token)
        }

        // then
        assertNotNull(exception)
        assertNotNull(exception.message)
        assertTrue(exception.message!!.contains("expired"))
    }

    fun getUserPassword(email: String): String? {
        val query = "select u.password from users u where u.uid = ?"
        val args = arrayOf(email)
        val argTypes = intArrayOf(Types.VARCHAR)
        return jdbcTemplate.queryForObject(query, args, argTypes, String::class.java)
    }

    fun getTokenId(token: String): Long? {
        val query = "select t.id from reset_password_tokens t where t.id = ?"
        val args = arrayOf(token)
        val argTypes = intArrayOf(Types.VARCHAR)
        val result = jdbcTemplate.query(query, args, argTypes) { rs, _ -> rs.getLong("id") }
        return if (result.isEmpty()) null else result[0]
    }
}