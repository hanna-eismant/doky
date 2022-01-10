package org.hkurh.doky.controllers;

import org.springframework.lang.NonNull;

public class ErrorResponse {

    private Error error;

    public ErrorResponse() {
    }

    public ErrorResponse(@NonNull final String message) {
        this.error = new Error(message);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Error {

        private String message;

        public Error() {
        }

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
