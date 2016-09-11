package com.aporlaoferta.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hasiermetal on 11/09/16.
 */
public class InvalidUserException extends Exception {

    private static final long serialVersionUID = -8462197181066331479L;

    private final List<Object> errorValues = new ArrayList<>();
    private final boolean inline;

    public InvalidUserException(String key) {
        this(false, key);
    }

    public InvalidUserException(boolean inline, String messageKey, Object... args) {
        super(messageKey);
        this.inline = inline;
        Collections.addAll(this.errorValues, args);
    }

    public boolean isInline() {
        return this.inline;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
