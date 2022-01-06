package org.hkurh.doky.controllers;

import org.springframework.lang.NonNull;

public class ErrorResponse {
    private final Error error;

    public ErrorResponse(@NonNull final String message) {
        this.error = new Error(message);
    }

    public Error getError() {
        return error;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Error {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
