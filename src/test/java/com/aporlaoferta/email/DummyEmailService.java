package com.aporlaoferta.email;

import com.aporlaoferta.model.TheUser;

/**
 * Created by hin01 on 08/09/15.
 */
public class DummyEmailService implements EmailService {

    @Override
    public void sendAccountConfirmationEmail(TheUser theUser) throws EmailSendingException {
        //do nothing
    }

    @Override
    public void sendPasswordForgotten(TheUser theUser) throws EmailSendingException {
        //do nothing
    }
}
