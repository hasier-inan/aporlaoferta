package com.aporlaoferta.controller;

import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.email.EmailSendingException;
import com.aporlaoferta.email.EmailService;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.CaptchaHTTPManager;
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
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @Mock
    CaptchaHTTPManager captchaHTTPManager;
    @Mock
    EmailService emailService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.userManager.doesUserExist(anyString())).thenReturn(false);
        when(this.captchaHTTPManager.validHuman(anyString())).thenReturn(true);
    }

    @Test
    public void testCreateUserCantAddUserToDBReturnsMessage() {
        when(this.userManager.createUser(any(TheUser.class))).thenReturn(null);
        TheUser user = UserBuilderManager.aRegularUserWithNickname("fu1").build();
        TheResponse result = this.accountController.createUser(
                user, "recaptcha");
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

    @Test
    public void testUserConfirmationWithEmptyValuesReturnsInvalidUUIDResponse() throws Exception {
        ModelAndView modelAndViewNoUUID = this.accountController.confirmUser("", "uuid");
        Assert.assertThat("Expected the message to contain the invalid uuid log", (String) modelAndViewNoUUID.getModel().get("msg"), startsWith("Id de confirmación inválido"));
        ModelAndView modelAndViewNoNickname = this.accountController.confirmUser("sdsd", "");
        Assert.assertThat("Expected the message to contain the invalid uuid log", (String) modelAndViewNoNickname.getModel().get("msg"), startsWith("Id de confirmación inválido"));
    }

    @Test
    public void testUserConfirmationWithCorrectDataReturnsValidConfirmationResponse() throws Exception {
        when(this.userManager.confirmUser("nick", "uuuuid")).thenReturn(ResponseResultHelper.createUserConfirmationResponse());
        ModelAndView modelAndViewNoUUID = this.accountController.confirmUser("uuuuid", "nick");
        Assert.assertThat("Expected the message to contain the confirmation uuid log", (String) modelAndViewNoUUID.getModel().get("msg"), startsWith("Usuario confirmado"));
    }

    @Test
    public void testSuccessfulUserCreationTriggersEmail() throws Exception, EmailSendingException {
        TheUser theUser = UserBuilderManager.aPendingUserWithNickname("theUser").build();
        when(this.userManager.createUser(theUser)).thenReturn(theUser);
        this.accountController.createUser(theUser, "recaptcha");
        verify(this.emailService, times(1)).sendAccountConfirmationEmail(any(TheUser.class));
    }
}
