package com.aporlaoferta.controller;


import com.aporlaoferta.dao.UserDAO;
import com.aporlaoferta.data.UserBuilderManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(
        {"classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml",
                "classpath:aporlaoferta-hibernate-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerRealTestIntegration {

    @Autowired
    UserDAO userDAO;

    @Test
    public void testRealCreateUser() throws Exception {
        this.userDAO.save(UserBuilderManager
                .aRegularUserWithNickname("pito")
                .withPassword("pito")
                .build());
    }

}