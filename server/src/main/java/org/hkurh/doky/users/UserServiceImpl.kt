package org.hkurh.doky.users

import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userEntityRepository: UserEntityRepository) : UserService {
    override fun findUserByUid(@NonNull userUid: String?): UserEntity? {
        return userEntityRepository.findByUid(userUid) ?: throw DokyNotFoundException("User doesn't exist")
    }

    override fun checkUserExistence(@NonNull userUid: String?): Boolean {
        val userEntity = userEntityRepository.findByUid(userUid)
        return userEntity != null
    }

    override fun create(@NonNull userUid: String?, @NonNull encodedPassword: String?): UserEntity? {
        val userEntity = UserEntity()
        userEntity.uid = userUid
        userEntity.password = encodedPassword
        val createdUser = userEntityRepository.save(userEntity)
        LOG.debug(String.format("Created new user [%s]", createdUser.id))
        return createdUser
    }

    @get:NonNull
    override val currentUser: UserEntity?
        get() {
            val name = SecurityContextHolder.getContext().authentication.name
            val userEntity = userEntityRepository.findByUid(name)
            LOG.debug(String.format("Get current user [%s]", userEntity!!.id))
            return userEntity
        }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }
}
