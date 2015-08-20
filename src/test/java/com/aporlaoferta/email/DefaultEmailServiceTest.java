package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.Email;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:45
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultEmailServiceTest {

    DefaultEmailService emailService;

    @Mock
    private EmailSendingService mailSenderService;
    @Mock
    private VelocityEngine velocity;
    @Mock
    private EmailTemplateDAO emailTemplateDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.emailService = new DefaultEmailService(this.mailSenderService,
                this.velocity , "httpBlah"
        );
        this.emailService.setEmailTemplateDAO(this.emailTemplateDAO);
        Mockito.when(this.emailTemplateDAO.findByName(anyString())).thenReturn(
                EmailTemplateBuilderManager.aBasicEmailTemplate("tehTemplateName").build());
    }

    @Test
    public void testEmailSenderServiceIsCalled() throws Exception, EmailSendingException {
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
        Mockito.verify(this.mailSenderService).sendEmail(any(Email.class));
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfNoTemplateIsFound() throws Exception, EmailSendingException {
        //IllegalArgumentException
        Mockito.when(this.emailTemplateDAO.findByName(anyString())).thenReturn(null);
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfVelocityTemplateIsNotFound() throws Exception, EmailSendingException {
        //ResourceNotFoundException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(ResourceNotFoundException.class);
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfParseExceptionIsThrownWhileEvaluationVelocityTemplate() throws Exception, EmailSendingException {
        //ParseErrorException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(ParseErrorException.class);
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfMethodExceptionIsThrownWhileEvaluationVelocityTemplate() throws Exception, EmailSendingException {
        //MethodInvocationException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(MethodInvocationException.class);
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
    }

    private TheUser createMockUSer() {
        return UserBuilderManager.aPendingUserWithNickname("moko").build();
    }

}
