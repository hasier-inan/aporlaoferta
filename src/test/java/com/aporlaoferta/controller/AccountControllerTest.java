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
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
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

    @Test
    public void testUserPasswordUpdateIsPerformedWithValidData() throws Exception {
        TheResponse theResponse = performPasswordUpdate();
        Assert.assertThat("Expected an ok result", theResponse.getCode(), is(0));
    }

    @Test
    public void testUserPasswordIsUpdated() throws Exception {
        performPasswordUpdate();
        ArgumentCaptor<TheUser> theUserArgumentCaptor = ArgumentCaptor.forClass(TheUser.class);
        Mockito.verify(this.userManager).updateUser(theUserArgumentCaptor.capture());
        TheUser theUser = theUserArgumentCaptor.getValue();
        Assert.assertThat("Expected theUser password to be updated",
                theUser.getUserPassword(),
                not("pass1"));
    }

    private String encryptPassword(String original) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        return bCryptPasswordEncoder.encode(original);
    }

    @Test
    public void testUserPasswordUpdateIsNotPerformedIfPasswordDontMatch() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithInvalidPasswords();
        Assert.assertThat("Expected an invalid password result", theResponse.getCode(), is(4));
    }

    @Test
    public void testPasswordUpdateReturnsExpectedCodeIfUserDoesNotExist() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithUnknownUser();
        Assert.assertThat("Expected an invalid data result", theResponse.getCode(), is(5));
    }

    @Test
    public void testPasswordUpdateReturnsExpectedCodeIfInvalidUserIsProvided() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithInvalidUser();
        Assert.assertThat("Expected an invalid data result", theResponse.getCode(), is(5));
    }

    private TheResponse performPasswordUpdate() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname, security_code);
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(theUser);
        when(this.userManager.updateUser(any(TheUser.class))).thenReturn(theUser);
        return this.accountController.passwordUpdate(nickname, "pass1", "pass1", security_code);
    }

    private TheResponse performPasswordUpdateWithInvalidPasswords() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname, security_code);
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(theUser);
        return this.accountController.passwordUpdate(nickname, "pass1", "pass2", security_code);
    }

    private TheResponse performPasswordUpdateWithUnknownUser() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(null);
        return this.accountController.passwordUpdate(nickname, "pass1", "pass1", security_code);
    }

    private TheResponse performPasswordUpdateWithInvalidUser() {
        String nickname1 = "the_user_nickname";
        String nickname2 = "the_user_nickkknameimtryingtosteal";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname2, "another_security_code");
        when(this.userManager.getUserFromNickname(nickname1)).thenReturn(theUser);
        return this.accountController.passwordUpdate(nickname1, "pass1", "pass1", security_code);
    }

    private TheUser createDummyPasswordUpdateUser(String nickname, String security_code) {
        return UserBuilderManager.aRegularUserWithNickname(nickname)
                .withUUID(security_code)
                .build();
    }

}
