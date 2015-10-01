package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class DefaultEmailSendingServiceTestIntegration {

    private static final String USER_CONFIRMATION = "AccountConfirmation";
    private static final String PASSWORD_RESET_EMAIL = "PasswordReset";
    private static final String NICKNAME = "DAnICKNAME";
    private static final String UUIDURL = "129108231827381273812";
    private static final String AVATARSRC = "http://www.dsaimage.com/kaka.png";
    @Autowired
    EmailTemplateDAO emailTemplateDAO;
    @Autowired
    VelocityEngine velocity;
    @Autowired
    DefaultEmailService defaultEmailService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addUserConfirmationTemplate(USER_CONFIRMATION);
        addPasswordForgottenTemplate(PASSWORD_RESET_EMAIL);
    }

    @Ignore
    @Test
    public void testEmailIsSent() throws Exception, EmailSendingException {
        TheUser user = createDummyUser();
        this.defaultEmailService.sendAccountConfirmationEmail(user);
    }

    private TheUser createDummyUser() {
        return UserBuilderManager.aPendingUserWithNickname(NICKNAME)
                .withAvatar(AVATARSRC)
                .withUUID(UUIDURL)
                .withEmail("hasiermetal@gmail.com")
                .build();
    }

    private void addUserConfirmationTemplate(String templateName) {
        this.emailTemplateDAO.save(createRealEmailConfirmationTemplate(templateName));
    }

    private void addPasswordForgottenTemplate(String templateName) {
        this.emailTemplateDAO.save(createRealPasswordForgottenTemplate(templateName));
    }

    private EmailTemplate createRealEmailConfirmationTemplate(String templateName) {
        return EmailTemplateBuilderManager.aRealConfirmationEmailTemplate(templateName).build();
    }

    private EmailTemplate createRealPasswordForgottenTemplate(String templateName) {
        return EmailTemplateBuilderManager.aRealPassowrdForgottenEmailTemplate(templateName).build();
    }
}
