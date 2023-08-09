package org.hkurh.doky.errorhandling;

public class DokyNotFoundException extends RuntimeException {

    public DokyNotFoundException(String message) {
        super(message);
    }
}
