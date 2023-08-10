package org.hkurh.doky.errorhandling;

public class DokyRegistrationException extends RuntimeException {
    public DokyRegistrationException(String message) {
        super(message);
    }

    public DokyRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}