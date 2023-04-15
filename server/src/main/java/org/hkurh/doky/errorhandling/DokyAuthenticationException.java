package org.hkurh.doky.errorhandling;

import org.springframework.security.core.AuthenticationException;

public class DokyAuthenticationException extends AuthenticationException {
    public DokyAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DokyAuthenticationException(String msg) {
        super(msg);
    }
}
