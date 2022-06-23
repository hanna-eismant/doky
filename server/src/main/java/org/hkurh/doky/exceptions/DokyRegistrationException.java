package org.hkurh.doky.exceptions;

public class DokyRegistrationException extends RuntimeException {
    public DokyRegistrationException(final String message) {
        super(message);
    }

    public DokyRegistrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
