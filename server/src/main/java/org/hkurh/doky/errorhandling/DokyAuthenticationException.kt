package org.hkurh.doky.errorhandling

import org.springframework.security.core.AuthenticationException

class DokyAuthenticationException(msg: String?) : AuthenticationException(msg)
