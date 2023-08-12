package org.hkurh.doky.users

import org.hkurh.doky.MapperFactory.Companion.modelMapper
import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserFacadeImpl(private val userService: UserService, private val passwordEncoder: PasswordEncoder) : UserFacade {
    override fun checkCredentials(@NonNull userUid: String?, @NonNull password: String?) {
        if (!userService.checkUserExistence(userUid)) {
            throw DokyAuthenticationException("User doesn't exist")
        }
        val userEntity = userService.findUserByUid(userUid)
        val encodedPassword = userEntity!!.password
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw DokyAuthenticationException("Incorrect credentials")
        }
    }

    override fun register(@NonNull username: String?, @NonNull password: String?): UserDto? {
        if (userService.checkUserExistence(username)) {
            throw DokyRegistrationException("User already exists")
        }
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userService.create(username, encodedPassword)
        LOG.info(String.format("Register new user [%s]", userEntity))
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    override val currentUser: UserDto
        get() {
            val userEntity = userService.currentUser
            return modelMapper.map(userEntity, UserDto::class.java) ?: UserDto()
        }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserFacadeImpl::class.java)
    }
}
