package org.hkurh.doky.users

import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.hkurh.doky.toDto
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserFacadeImpl(private val userService: UserService, private val passwordEncoder: PasswordEncoder) : UserFacade {
    override fun checkCredentials(userUid: String, password: String) {
        if (!userService.checkUserExistence(userUid)) throw DokyAuthenticationException("User doesn't exist")
        val userEntity = userService.findUserByUid(userUid)
        val encodedPassword = userEntity!!.password
        if (!passwordEncoder.matches(password, encodedPassword)
        ) throw DokyAuthenticationException("Incorrect credentials")
    }

    override fun register(username: String, password: String): UserDto {
        if (userService.checkUserExistence(username)) throw DokyRegistrationException("User already exists")
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userService.create(username, encodedPassword)
        LOG.info("Register new user $userEntity")
        return userEntity.toDto()
    }

    override fun getCurrentUser(): UserDto {
        return userService.getCurrentUser().toDto()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserFacadeImpl::class.java)
    }
}
