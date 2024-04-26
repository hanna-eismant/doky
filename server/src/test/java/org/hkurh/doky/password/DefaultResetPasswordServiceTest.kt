package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenRepository
import org.hkurh.doky.password.impl.DefaultResetPasswordService
import org.hkurh.doky.password.impl.DefaultTokenService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*


@DisplayName("DefaultResetPasswordService unit test")
class DefaultResetPasswordServiceTest : DokyUnitTest {

    private var tokenUtil: DefaultTokenService = mock()
    private var resetPasswordTokenRepository: ResetPasswordTokenRepository = mock()
    private var resetPasswordService = DefaultResetPasswordService(tokenUtil, resetPasswordTokenRepository)

    private val mockUser = mock<UserEntity>()
    private val tokenId: Long = 1
    private val tokenString = "test-token"
    private val tokenDate = Date()

    @Test
    @DisplayName("Should save token info in database")
    fun shouldSaveTokenInfoInDatabase() {
        // when
        whenever(tokenUtil.generateToken()).thenReturn(tokenString)
        whenever(tokenUtil.calculateExpirationDate()).thenReturn(tokenDate)

        whenever(resetPasswordTokenRepository.save(any<ResetPasswordTokenEntity>())).thenAnswer { invocation ->
            val argument = invocation.getArgument<ResetPasswordTokenEntity>(0)
            assertEquals(mockUser, argument.user)
            assertEquals(tokenString, argument.token)
            assertEquals(tokenDate, argument.expirationDate)
            argument.id = tokenId
            argument
        }

        // when
        val token = resetPasswordService.generateAndSaveResetToken(mockUser)

        // then
        assertEquals(token, tokenString)
        verify(resetPasswordTokenRepository, times(1)).save(any<ResetPasswordTokenEntity>())
    }


    @Test
    @DisplayName("Should remove existing token and save new token in database")
    fun shouldRemoveExistingAndSaveNewTokenInformationInDatabase() {
        // Given
        val existingToken = ResetPasswordTokenEntity()
        existingToken.id = tokenId
        existingToken.token = "existing-token"

        whenever(tokenUtil.generateToken()).thenReturn(tokenString)
        whenever(tokenUtil.calculateExpirationDate()).thenReturn(tokenDate)
        whenever(resetPasswordTokenRepository.findByUser(mockUser)).thenReturn(existingToken)

        whenever(resetPasswordTokenRepository.save(any<ResetPasswordTokenEntity>())).thenAnswer { invocation ->
            val argument = invocation.getArgument<ResetPasswordTokenEntity>(0)
            assertEquals(mockUser, argument.user)
            assertEquals(tokenString, argument.token)
            assertEquals(tokenDate, argument.expirationDate)
            argument.id = tokenId
            argument
        }

        // When
        val token = resetPasswordService.generateAndSaveResetToken(mockUser)

        // Then
        verify(resetPasswordTokenRepository, times(1)).findByUser(mockUser)
        verify(resetPasswordTokenRepository, times(1)).delete(existingToken)

        assertEquals(token, tokenString)
    }
}
