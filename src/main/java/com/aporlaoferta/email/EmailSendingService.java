package com.aporlaoferta.email;

import com.aporlaoferta.model.Email;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:53
 */
public interface EmailSendingService {
    public void sendEmail(Email email) throws EmailSendingException;
}
