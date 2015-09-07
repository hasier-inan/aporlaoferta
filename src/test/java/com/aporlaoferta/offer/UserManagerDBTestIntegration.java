package com.aporlaoferta.offer;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.data.EmailTemplateBuilderManager;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.UserManager;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
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

    private static final String TEMPLATE_NAME = "AccountConfirmation";

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
        addDefaultTemplate(TEMPLATE_NAME);
    }

    private void addDefaultTemplate(String templateName) {
        this.emailTemplateDAO.save(createDummyTemplate(templateName));
    }

    private EmailTemplate createDummyTemplate(String templateName) {
        return EmailTemplateBuilderManager.aBasicEmailTemplate(templateName).build();
    }

}

