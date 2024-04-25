package org.hkurh.doky.password

import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenRepository
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class ResetPasswordService(
    private var tokenUtil: TokenUtil,
    private val resetPasswordTokenRepository: ResetPasswordTokenRepository
) {

    fun generateAndSaveResetToken(user: UserEntity): String {
        resetPasswordTokenRepository.findByUser(user)?.let {
            resetPasswordTokenRepository.delete(it)
        }
        val token = tokenUtil.generateToken()
        val expirationDate = tokenUtil.calculateExpirationDate()
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.user = user
            this.expirationDate = expirationDate
        }
        val savedPasswordTokenEntity = resetPasswordTokenRepository.save(resetPasswordTokenEntity)
        return savedPasswordTokenEntity.token!!
    }
}
