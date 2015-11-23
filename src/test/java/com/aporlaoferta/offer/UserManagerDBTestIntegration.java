package com.aporlaoferta.offer;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.UserManager;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-hibernate-context.xml", "classpath:aporlaoferta-managers-test-context.xml"})
public class UserManagerDBTestIntegration {

    private static final String ACCOUNT_CONFIRMATION = "AccountConfirmation";
    private static final String PASSWORD_FORGOTTEN = "PasswordReset";

    @Autowired
    private UserManager userManager;

    @Autowired
    private EmailTemplateDAO emailTemplateDAO;

    @Ignore
    @Test
    public void testCustomUserIsConfirmed() throws Exception {
        TheUser n = this.userManager.getUserFromNickname("n");
        this.userManager.confirmUser(n.getUuid(), n.getUserNickname());
    }

    @Ignore
    @Test
    public void testTemplateIsCreated() throws Exception {
        this.emailTemplateDAO.save(getAccountConfirmationTemplate());
        this.emailTemplateDAO.save(getPasswordForgottenTemplate());
    }

    private EmailTemplate getPasswordForgottenTemplate() {
        return EmailTemplateBuilderManager.aRealPassowrdForgottenEmailTemplate(PASSWORD_FORGOTTEN).build();
    }

    private EmailTemplate getAccountConfirmationTemplate() {
        return EmailTemplateBuilderManager.aRealConfirmationEmailTemplate(ACCOUNT_CONFIRMATION).build();
    }

}

