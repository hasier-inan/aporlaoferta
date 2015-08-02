package com.aporlaoferta.model.validators;

/**
 * Created by hasiermetal on 15/01/15.
 */
public enum ValidationError {
    ERROR_FIELD_FORMAT("error_field_format"),
    ERROR_FIELD_REQUIRED("error_field_required"),
    ERROR_FIELD_LENGTH_EXCEED("error_field_length");
    private String code;

    private ValidationError(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
