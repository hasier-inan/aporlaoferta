package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
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
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultEmailSendingServiceTestIntegration {

    private static final String TEMPLATE_NAME = "AccountConfirmation";
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
        addDefaultTemplate(TEMPLATE_NAME);
    }

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

    private void addDefaultTemplate(String templateName) {
        this.emailTemplateDAO.save(createRealEmailConfirmationTemplate(templateName));
    }

    private EmailTemplate createRealEmailConfirmationTemplate(String templateName) {
        return EmailTemplateBuilderManager.aRealConfirmationEmailTemplate(templateName).build();
    }
}
