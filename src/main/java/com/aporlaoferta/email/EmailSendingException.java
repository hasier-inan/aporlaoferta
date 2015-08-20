package com.aporlaoferta.email;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 16:02
 */
public class EmailSendingException extends Throwable {
    private static final long serialVersionUID = 1L;

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
