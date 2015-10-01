package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.Email;
import com.aporlaoferta.model.ServerValue;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
        this.emailService = new DefaultEmailService();
        this.emailService.setMailSenderService(this.mailSenderService);
        this.emailService.setVelocity(this.velocity);
        this.emailService.setServerValue(new ServerValue("httpBlah"));
        this.emailService.setEmailTemplateDAO(this.emailTemplateDAO);
        Mockito.when(this.emailTemplateDAO.findByName(anyString())).thenReturn(
                EmailTemplateBuilderManager.aBasicEmailTemplate("tehTemplateName").build());
    }

    @Test
    public void testEmailSenderServiceIsCalled() throws Exception, EmailSendingException {
        this.emailService.sendEmailBasedOnTemplate(createMockUSer(), "template1");
        Mockito.verify(this.mailSenderService).sendEmail(any(Email.class));
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfNoTemplateIsFound() throws Exception, EmailSendingException {
        //IllegalArgumentException
        Mockito.when(this.emailTemplateDAO.findByName(anyString())).thenReturn(null);
        this.emailService.sendEmailBasedOnTemplate(createMockUSer(), "tempalte1");
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfVelocityTemplateIsNotFound() throws Exception, EmailSendingException {
        //ResourceNotFoundException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(ResourceNotFoundException.class);
        this.emailService.sendEmailBasedOnTemplate(createMockUSer(), "wahatever_teh_tmplt");
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfParseExceptionIsThrownWhileEvaluationVelocityTemplate() throws Exception, EmailSendingException {
        //ParseErrorException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(ParseErrorException.class);
        this.emailService.sendEmailBasedOnTemplate(createMockUSer(), "tempalte1");
    }

    @Test(expected = EmailSendingException.class)
    public void testEmailSendingExceptionIsThrownIfMethodExceptionIsThrownWhileEvaluationVelocityTemplate() throws Exception, EmailSendingException {
        //MethodInvocationException
        Mockito.when(this.velocity.evaluate(any(Context.class), any(java.io.Writer.class), anyString(), anyString()))
                .thenThrow(MethodInvocationException.class);
        this.emailService.sendEmailBasedOnTemplate(createMockUSer(), "template1");
    }

    @Test
    public void testExpectedTemplateIsSentForConfirmationEmail() throws Exception, EmailSendingException {
        this.emailService.sendAccountConfirmationEmail(createMockUSer());
        ArgumentCaptor<String> theTemplateArgumentCaptor = getTemplateFromArgumentCaptor();
        assertThat("Expected confirmation template", theTemplateArgumentCaptor.getValue(), is("AccountConfirmation"));
    }

    @Test
    public void testExpectedTemplateIsSentForPasswordForgottenEmail() throws Exception, EmailSendingException {
        this.emailService.sendPasswordForgotten(createMockUSer());
        ArgumentCaptor<String> theTemplateArgumentCaptor = getTemplateFromArgumentCaptor();
        assertThat("Expected password forgotten template", theTemplateArgumentCaptor.getValue(), is("PasswordReset"));
    }

    private ArgumentCaptor<String> getTemplateFromArgumentCaptor() {
        ArgumentCaptor<String> theTemplateArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.emailTemplateDAO).findByName(theTemplateArgumentCaptor.capture());
        return theTemplateArgumentCaptor;
    }

    private TheUser createMockUSer() {
        return UserBuilderManager.aPendingUserWithNickname("moko").build();
    }

}
