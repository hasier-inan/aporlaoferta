package com.aporlaoferta.model.validators;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -8462197181066331479L;

    private final String messageKey;
    private final List<Object> errorValues = new ArrayList<>();
    private final boolean inline;

    /**
     * Constructor that takes a message key to resolve error message at later
     * stage.
     *
     * @param key - the resource bundle message key
     */
    public ValidationException(String key) {
        this(false, key);
    }

    /**
     * Constructor that takes the message key and application object.
     *
     * @param messageKey - the message key
     * @param args       - the error value/s e.g. the external loan ID
     */
    public ValidationException(String messageKey, Object... args) {
        this(false, messageKey, args);
    }

    /**
     * Constructor that takes the message key, application object and a boolean.
     *
     * @param inline     - indicates if the message key should be used in-line with
     *                   vendors
     * @param messageKey - the message key
     * @param args       - the error value/s e.g. the external loan ID
     */
    public ValidationException(boolean inline, String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.inline = inline;
        Collections.addAll(this.errorValues, args);
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public List<Object> getErrorValues() {
        return this.errorValues;
    }

    public boolean isInline() {
        return this.inline;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
