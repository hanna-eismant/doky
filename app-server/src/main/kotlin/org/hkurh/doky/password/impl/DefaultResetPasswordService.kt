package org.hkurh.doky.password.impl

import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.password.TokenService
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultResetPasswordService(
    private val tokenService: TokenService,
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository,
) : ResetPasswordService {

    override fun generateAndSaveResetToken(user: UserEntity): String {
        resetPasswordTokenEntityRepository.findByUser(user)?.let {
            resetPasswordTokenEntityRepository.delete(it)
        }
        val token = tokenService.generateToken()
        val expirationDate = tokenService.calculateExpirationDate()
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.user = user
            this.expirationDate = expirationDate
        }
        val savedPasswordTokenEntity = resetPasswordTokenEntityRepository.save(resetPasswordTokenEntity)
        return savedPasswordTokenEntity.token
    }

    override fun checkToken(token: String): ResetPasswordTokenEntity {
        val resetPasswordToken = resetPasswordTokenEntityRepository.findByToken(token)
            ?: throw DokyInvalidTokenException("Token to reset password is invalid")
        if (Date().after(resetPasswordToken.expirationDate))
            throw DokyInvalidTokenException("Token to reset password expired")
        return resetPasswordToken
    }

    override fun delete(resetPasswordToken: ResetPasswordTokenEntity) {
        resetPasswordTokenEntityRepository.delete(resetPasswordToken)
    }
}
