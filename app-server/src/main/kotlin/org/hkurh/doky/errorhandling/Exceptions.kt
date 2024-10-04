package org.hkurh.doky.errorhandling

import org.springframework.security.core.AuthenticationException

class DokyAuthenticationException(msg: String?) : AuthenticationException(msg)
class DokyNotFoundException(message: String?) : RuntimeException(message)
class DokyRegistrationException(message: String?) : RuntimeException(message)
class DokyInvalidTokenException(message: String?) : RuntimeException(message)
