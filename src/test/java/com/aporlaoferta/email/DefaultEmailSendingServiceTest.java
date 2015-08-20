package com.aporlaoferta.email;

import com.aporlaoferta.model.Email;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 12/08/15
 * Time: 11:34
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultEmailSendingServiceTest {

    private String FROM_ADDRESS = "YO";

    @Mock
    private JavaMailSender mailSender;

    private DefaultEmailSendingService defaultEmailSendingService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        makeMailSenderReturnEmptyMimeMessage();
        this.defaultEmailSendingService = new DefaultEmailSendingService(this.mailSender, FROM_ADDRESS);
    }

    @Test
    public void testEmailIsSentWithMimeMessage() throws Exception, EmailSendingException {
        this.defaultEmailSendingService.sendEmail(createDummyEmail());
        Mockito.verify(this.mailSender).send(Mockito.any(MimeMessage.class));
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownWhenEmailCantBeDelivered() throws Exception, EmailSendingException {
        doThrow(new DummyMailException("dummy")).when(this.mailSender).send(Mockito.any(MimeMessage.class));
        this.defaultEmailSendingService.sendEmail(createDummyEmail());
    }

    @Ignore
    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownWhenMimeMessageThrowsMessagingException() throws Exception, EmailSendingException {
    }

    @Ignore
    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownWhenMimeMessageThrowsMailException() throws Exception, EmailSendingException {

    }

    private void makeMailSenderReturnEmptyMimeMessage() {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        when(this.mailSender.createMimeMessage()).thenReturn(message);
    }

    private Email createDummyEmail() {
        return new Email("kaka@kaka.coo", "kaka", "<kaka>");
    }

    private class DummyMailException extends MailException {
        public DummyMailException(String msg) {
            super(msg);
        }
    }

}
