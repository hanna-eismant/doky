package org.hkurh.doky.users

import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.security.DokyUserDetails
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userEntityRepository: UserEntityRepository) : UserService {
    override fun findUserByUid(userUid: String): UserEntity? {
        return userEntityRepository.findByUid(userUid) ?: throw DokyNotFoundException("User doesn't exist")
    }

    override fun checkUserExistence(userUid: String): Boolean {
        val userEntity = userEntityRepository.findByUid(userUid)
        return userEntity != null
    }

    override fun create(userUid: String, encodedPassword: String): UserEntity {
        val userEntity = UserEntity()
        userEntity.uid = userUid
        userEntity.password = encodedPassword
        val createdUser = userEntityRepository.save(userEntity)
        LOG.debug("Created new user {}", createdUser.id)
        return createdUser
    }

    override fun getCurrentUser(): UserEntity {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        LOG.debug("Get current user ${userEntity!!.id}")
        return userEntity
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }
}
