package org.hkurh.doky.errorhandling;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse extends ErrorResponse {
    private List<Field> fields = new ArrayList<>();

    public ValidationErrorResponse(String message) {
        super(message);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public static class Field {
        private String field;
        private String message;

        public Field(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
