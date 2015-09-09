package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.Email;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:45
 */
@ContextConfiguration(
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml",
                "classpath:aporlaoferta-email-real-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultEmailServiceTestIntegration {

    private static final String TEMPLATE_NAME = "AccountConfirmation";
    private static final String NICKNAME = "DAnICKNAME";
    private static final String UUIDURL = "129108231827381273812";
    private static final String AVATARSRC = "http://www.dsaimage.com/kaka.png";
    @Autowired
    EmailTemplateDAO emailTemplateDAO;
    @Autowired
    VelocityEngine velocity;
    @Mock
    DefaultEmailSendingService mailSenderServiceMock;

    DefaultEmailService defaultEmailService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addDefaultTemplate(TEMPLATE_NAME);
        this.defaultEmailService = new DefaultEmailService(mailSenderServiceMock, velocity, "httpblah");
        this.defaultEmailService.setEmailTemplateDAO(this.emailTemplateDAO);
    }

    @Test
    public void testUserDetailsAreIncludedInTheTemplate() throws Exception, EmailSendingException {
        TheUser user = createDummyUser();
        ArgumentCaptor<Email> emailArgumentCaptor = ArgumentCaptor.forClass(Email.class);
        this.defaultEmailService.sendAccountConfirmationEmail(user);
        verifyEmailContainsAllUserData(emailArgumentCaptor, user);
    }

    private void verifyEmailContainsAllUserData(ArgumentCaptor<Email> emailArgumentCaptor, TheUser user) throws EmailSendingException {
        Mockito.verify(this.mailSenderServiceMock).sendEmail(emailArgumentCaptor.capture());
        Email sentEmail = emailArgumentCaptor.getValue();
        Assert.assertThat("Expected user email to be the recipient email", sentEmail.toAddress(), is(user.getUserEmail()));
        Assert.assertThat("Expected user name to be found in the email", sentEmail.content().indexOf(NICKNAME), greaterThan(0));
        Assert.assertThat("Expected user uuid url to be found in the email", sentEmail.content().indexOf(UUIDURL), greaterThan(0));
        Assert.assertThat("Expected user avatar img url to be found in the email", sentEmail.content().indexOf(AVATARSRC), greaterThan(0));
    }

    private TheUser createDummyUser() {
        return UserBuilderManager.aPendingUserWithNickname(NICKNAME)
                .withAvatar(AVATARSRC)
                .withUUID(UUIDURL)
                .build();
    }

    private void addDefaultTemplate(String templateName) {
        this.emailTemplateDAO.save(createDummyTemplate(templateName));
    }

    private EmailTemplate createDummyTemplate(String templateName) {
        return EmailTemplateBuilderManager.aBasicEmailTemplate(templateName).build();
    }
}
