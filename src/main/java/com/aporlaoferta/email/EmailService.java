package com.aporlaoferta.email;

import com.aporlaoferta.model.TheUser;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:44
 */
@Service
public interface EmailService {
    void sendAccountConfirmationEmail(TheUser theUser) throws EmailSendingException;
    void sendPasswordForgotten(TheUser theUser) throws EmailSendingException;
}
