package org.hkurh.doky.users.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.hkurh.doky.security.DokySecurityService
import org.hkurh.doky.toDto
import org.hkurh.doky.users.UserFacade
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.api.UpdateUserRequest
import org.hkurh.doky.users.api.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DefaultUserFacade(
    private val userService: UserService,
    private val dokySecurityService: DokySecurityService,
    private val passwordEncoder: PasswordEncoder
) : UserFacade {
    override fun checkCredentials(userUid: String, password: String): UserDto {
        if (!userService.exists(userUid)) throw DokyAuthenticationException("User doesn't exist")
        val userEntity = userService.findUserByUid(userUid)
        val encodedPassword = userEntity!!.password
        if (!passwordEncoder.matches(password, encodedPassword))
            throw DokyAuthenticationException("Incorrect credentials")
        return userEntity.toDto()
    }

    override fun register(userUid: String, password: String): UserDto {
        if (userService.exists(userUid)) throw DokyRegistrationException("User already exists")
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userService.create(userUid, encodedPassword)
        LOG.info { "Register new user [${userEntity.id}]" }
        return userEntity.toDto()
    }

    override fun getCurrentUser(): UserDto {
        return dokySecurityService.getCurrentUser().toDto()
    }

    override fun updateCurrentUser(updateUserRequest: UpdateUserRequest) {
        val currentUser = dokySecurityService.getCurrentUser()
        currentUser.name = updateUserRequest.name?.ifEmpty { currentUser.name } ?: updateUserRequest.name
        userService.updateUser(currentUser)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
