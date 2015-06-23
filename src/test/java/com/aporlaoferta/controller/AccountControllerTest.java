package com.aporlaoferta.controller;

import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    AccountController accountController;
    @Mock
    OfferValidatorHelper offerValidatorHelper;
    @Mock
    UserManager userManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.userManager.doesUserExist(anyString())).thenReturn(false);
    }

    @Test
    public void testCreateUserCantAddUserToDBReturnsMessage() {
        when(this.userManager.createUser(any(TheUser.class))).thenReturn(null);
        TheResponse result = this.accountController.createUser(
                UserBuilderManager.aRegularUserWithNickname("fu1").build());
        Assert.assertThat("Expected empty object message", result.getResponseResult(),
                Matchers.is(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResponseResult()));
    }

    @Test
    public void testAccountDetailsReturnsUserWithNoPassword() throws Exception {
        when(this.userManager.getUserNickNameFromSession()).thenReturn("f");
        when(this.userManager.getUserFromNickname("f")).thenReturn(UserBuilderManager.aRegularUserWithNickname("f").build());
        TheUser theUserResult = this.accountController.accountDetails();
        Assert.assertEquals("Expected user details with empty password", theUserResult.getUserPassword(), "");
    }

    @Test
    public void testEmptyUserIsReturnedIfUserNotLoggedIn() throws Exception {
        when(this.userManager.getUserNickNameFromSession()).thenReturn(null);
        TheUser theUserResult = this.accountController.accountDetails();
        Assert.assertTrue("Expected user details to be empty", isEmpty(theUserResult.getUserNickname()));


    }
}
