package com.aporlaoferta.email;

import com.aporlaoferta.model.TheUser;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:44
 */
public interface EmailService {
    public void sendAccountConfirmationEmail(TheUser theUser) throws EmailSendingException;
}
