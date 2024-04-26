package org.hkurh.doky.password.impl

import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.password.TokenService
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenRepository
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultResetPasswordService(
    private var tokenService: TokenService,
    private val resetPasswordTokenRepository: ResetPasswordTokenRepository
) : ResetPasswordService {

    override fun generateAndSaveResetToken(user: UserEntity): String {
        resetPasswordTokenRepository.findByUser(user)?.let {
            resetPasswordTokenRepository.delete(it)
        }
        val token = tokenService.generateToken()
        val expirationDate = tokenService.calculateExpirationDate()
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.user = user
            this.expirationDate = expirationDate
        }
        val savedPasswordTokenEntity = resetPasswordTokenRepository.save(resetPasswordTokenEntity)
        return savedPasswordTokenEntity.token!!
    }
}
