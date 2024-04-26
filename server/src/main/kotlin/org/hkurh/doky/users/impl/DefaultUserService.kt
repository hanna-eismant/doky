package org.hkurh.doky.users.impl

import org.hkurh.doky.email.EmailService
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.security.DokyUserDetails
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DefaultUserService(
    private val userEntityRepository: UserEntityRepository,
    private val emailService: EmailService
) : UserService {
    override fun findUserByUid(userUid: String): UserEntity? {
        return userEntityRepository.findByUid(userUid) ?: throw DokyNotFoundException("User doesn't exist")
    }

    override fun create(userUid: String, encodedPassword: String): UserEntity {
        val userEntity = UserEntity()
        userEntity.uid = userUid
        userEntity.password = encodedPassword
        userEntity.name = extractNameFromUid(userUid)
        val createdUser = userEntityRepository.save(userEntity)
        LOG.debug("Created new user ${createdUser.id}")
        try {
            emailService.sendRegistrationConfirmationEmail(createdUser)
        } catch (e: MailException) {
            LOG.error("Error during sending registration email for user [${createdUser.id}]", e)
        }
        return createdUser
    }

    override fun getCurrentUser(): UserEntity {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        LOG.debug("Get current user ${userEntity!!.id}")
        return userEntity
    }

    override fun updateUser(user: UserEntity) {
        userEntityRepository.save(user)
        LOG.debug("User is updated ${user.id}")
    }

    private fun extractNameFromUid(userUid: String): String {
        return userUid.split("@").first()
    }

    override fun exists(email: String): Boolean {
        return userEntityRepository.existsByUid(email)
    }

    override fun exists(id: Long): Boolean {
        return userEntityRepository.existsById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DefaultUserService::class.java)
    }
}