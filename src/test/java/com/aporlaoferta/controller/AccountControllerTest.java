package com.aporlaoferta.controller;

import com.aporlaoferta.data.ForgettableUserBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.email.EmailSendingException;
import com.aporlaoferta.email.EmailService;
import com.aporlaoferta.model.TheForgettableUser;
import com.aporlaoferta.model.TheEnhancedUser;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doThrow;
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
        when(this.userManager.isUserPending(any(TheUser.class)))
                .thenReturn(false);
        when(this.userManager.userIsBanned()).thenReturn(false);
        when(this.captchaHTTPManager.validHuman(anyString())).thenReturn(true);
    }

    @Test
    public void testCreateUserCantAddUserToDBReturnsMessage() {
        when(this.userManager.createUser(any(TheUser.class))).thenReturn(null);
        TheEnhancedUser user = UserBuilderManager.aRegularEnhancedUserWithNickname("fu1").build();
        TheResponse result = this.accountController.createUser(
                user, "recaptcha");
        assertThat("Expected empty object message", result.getResponseResult(),
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
        assertThat("Expected the message to contain the invalid uuid log", (String) modelAndViewNoUUID.getModel().get("msg"), startsWith("Identificador de confirmación inválido"));
        ModelAndView modelAndViewNoNickname = this.accountController.confirmUser("sdsd", "");
        assertThat("Expected the message to contain the invalid uuid log", (String) modelAndViewNoNickname.getModel().get("msg"), startsWith("Identificador de confirmación inválido"));
    }

    @Test
    public void testUserConfirmationWithCorrectDataReturnsValidConfirmationResponse() throws Exception {
        when(this.userManager.confirmUser("nick", "uuuuid")).thenReturn(ResponseResultHelper.createUserConfirmationResponse());
        ModelAndView modelAndViewNoUUID = this.accountController.confirmUser("uuuuid", "nick");
        assertThat("Expected the message to contain the confirmation uuid log", (String) modelAndViewNoUUID.getModel().get("msg"), startsWith("Usuario confirmado"));
    }

    @Test
    public void testPasswordForgottenDisplaysView() throws Exception {
        ModelAndView passwordForgotten = this.accountController.passwordForgotten("name", "uuid");
        assertThat("Expected the passwordForgotten view", passwordForgotten.getViewName(), is("passwordForgotten"));
        assertThat("Expected the passwordForgotten view", (String) passwordForgotten.getModel().get("nick"), is("name"));
        assertThat("Expected the passwordForgotten view", (String) passwordForgotten.getModel().get("uuid"), is("uuid"));
    }

    @Test
    public void testSuccessfulUserCreationTriggersEmail() throws Exception, EmailSendingException {
        TheEnhancedUser theUser = UserBuilderManager.aPendingEnhancedUserWithNickname("theUser").build();
        TheUser thePendingUser = UserBuilderManager.aPendingUserWithNickname("theUser").build();
        when(this.userManager.createUser(any(TheUser.class))).thenReturn(thePendingUser);
        this.accountController.createUser(theUser, "recaptcha");
        verify(this.emailService, times(1)).sendAccountConfirmationEmail(any(TheUser.class));
    }

    @Test
    public void testUserPasswordUpdateIsPerformedWithValidData() throws Exception {
        TheResponse theResponse = performPasswordUpdate();
        assertThat("Expected an ok result", theResponse.getCode(), is(0));
    }

    @Test
    public void testUserPasswordIsUpdated() throws Exception {
        performPasswordUpdate();
        ArgumentCaptor<TheUser> theUserArgumentCaptor = ArgumentCaptor.forClass(TheUser.class);
        Mockito.verify(this.userManager).updateUser(theUserArgumentCaptor.capture(), anyBoolean());
        TheUser theUser = theUserArgumentCaptor.getValue();
        assertThat("Expected theUser password to be updated",
                theUser.getUserPassword(),
                not("pass1"));
    }

    @Test
    public void testUserPasswordUpdateIsNotPerformedIfPasswordDontMatch() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithInvalidPasswords();
        assertThat("Expected an invalid password result", theResponse.getCode(), is(4));
    }

    @Test
    public void testPasswordUpdateReturnsExpectedCodeIfUserDoesNotExist() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithUnknownUser();
        assertThat("Expected an invalid data result", theResponse.getCode(), is(5));
    }

    @Test
    public void testPasswordUpdateReturnsExpectedCodeIfInvalidUserIsProvided() throws Exception {
        TheResponse theResponse = performPasswordUpdateWithInvalidUser();
        assertThat("Expected an invalid data result", theResponse.getCode(), is(5));
    }

    @Test
    public void testForgottenPasswordRequestTriggersEmailIfEmailExists() throws Exception, EmailSendingException {
        String email = "nickname@email.com";
        TheUser user = UserBuilderManager.aRegularUserWithEmail(email).build();
        when(this.userManager.doesUserEmailExist(email))
                .thenReturn(true);
        when(this.userManager.getUserFromEmail(email))
                .thenReturn(user);
        TheResponse theResponse = this.accountController.requestForgottenPassword(email);
        verify(this.emailService).sendPasswordForgotten(user);
        assertThat("Expected an ok response", theResponse.getCode(), is(0));
    }

    @Test
    public void testForgottenPasswordReturnsInvalidUserIfDoesNotExist() throws Exception, EmailSendingException {
        String email = "nickname@email.com";
        when(this.userManager.getUserFromEmail(email)).thenReturn(null);
        TheResponse theResponse = this.accountController.requestForgottenPassword(email);
        assertThat("Expected invalid user email code", theResponse.getCode(), is(ResultCode.USER_EMAIL_IS_INVALID.getCode()));
    }

    @Test
    public void testForgottenPasswordReturnsInvalidUserIfItHasBeenBanned() throws Exception, EmailSendingException {
        String email = "nickname@email.com";
        when(this.userManager.getUserFromEmail(email)).thenReturn(
                UserBuilderManager.aRegularUserWithEmail(email).isEnabled(false).build());
        TheResponse theResponse = this.accountController.requestForgottenPassword(email);
        assertThat("Expected invalid user email code", theResponse.getCode(), is(ResultCode.USER_EMAIL_IS_INVALID.getCode()));
    }

    @Test
    public void testForgottenPasswordReturnsUnconfirmedUserIfItIsPending() throws Exception, EmailSendingException {
        TheUser user = UserBuilderManager.aRegularUserWithEmail("anunconfirmed@email.com").isPending(true).build();
        when(this.userManager.getUserFromEmail("anunconfirmed@email.com"))
                .thenReturn(user);
        TheResponse theResponse = this.accountController.requestForgottenPassword("anunconfirmed@email.com");
        assertThat("Expected unconfirmed email", theResponse.getCode(), is(ResultCode.USER_EMAIL_NOT_CONFIRMED.getCode()));
    }

    @Test
    public void testForgottenPasswordReturnsGenericErrorIfEmailFails() throws Exception, EmailSendingException {
        String email = "nickname@email.com";
        TheUser user = UserBuilderManager.aRegularUserWithEmail(email).build();
        when(this.userManager.getUserFromEmail(email))
                .thenReturn(user);

        doThrow(new EmailSendingException("", new Throwable())).when(this.emailService)
                .sendPasswordForgotten(user);
        TheResponse theResponse = this.accountController.requestForgottenPassword(email);
        assertThat("Expected generic error response", theResponse.getCode(), is(ResultCode.DEFAULT_ERROR.getCode()));
    }

    @Test
    public void testExistingEmailReturnsExpectedError() throws Exception {
        String existingEmail = "nickname@existing.com";
        when(this.userManager.doesUserEmailExist(existingEmail))
                .thenReturn(true);
        TheEnhancedUser theUser = UserBuilderManager.aRegularEnhancedUserWithEmail(existingEmail).build();
        TheResponse theResponse = this.accountController.createUser(theUser, "captcha");
        assertThat("Expected invalid user email code", theResponse.getCode(), is(6));
    }

    @Test
    public void testUpdateAccountIsForNonBannedUsersOnly() {
        when(this.userManager.userIsBanned()).thenReturn(true);
        TheResponse result = this.accountController.updateUser(
                new TheEnhancedUser(),
                "1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testBanUsertIsForAdminOnly() {
        when(this.userManager.isUserAdmin()).thenReturn(false);
        TheResponse result = this.accountController.banUser("aUserToBeBanned");
        assertTrue(ResultCode.DEFAULT_ERROR.getCode() == result.getCode());
    }

    @Test
    public void testBanUserWhenNicknameDoesNotExistReturnsValidationError() {
        when(this.userManager.isUserAdmin()).thenReturn(true);
        when(this.userManager.getUserFromNickname("aUserToBeBanned")).thenReturn(null);
        TheResponse result = this.accountController.banUser("aUserToBeBanned");
        assertTrue(ResultCode.UPDATE_USER_VALIDATION_ERROR.getCode() == result.getCode());
    }

    @Test
    public void testDeleteCommentIsReturnsOKResult() {
        TheUser aUserToBeBanned = UserBuilderManager.aRegularUserWithNickname("aUserToBeBanned").build();
        TheResponse mockedResult = new TheResponse();
        when(this.userManager.isUserAdmin()).thenReturn(true);
        when(this.userManager.getUserFromNickname("aUserToBeBanned")).thenReturn(aUserToBeBanned);
        when(this.userManager.banUser(aUserToBeBanned)).thenReturn(mockedResult);
        TheResponse result = this.accountController.banUser("aUserToBeBanned");
        assertTrue(mockedResult == result);
    }

    private TheResponse performPasswordUpdate() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname, security_code);
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(theUser);
        when(this.userManager.updateUser(any(TheUser.class), anyBoolean())).thenReturn(theUser);
        TheForgettableUser forgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(nickname, security_code).build();
        return this.accountController.passwordUpdate(forgettableUser);
    }

    private TheResponse performPasswordUpdateWithInvalidPasswords() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname, security_code);
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(theUser);
        TheForgettableUser forgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithDifferentPass(nickname, security_code).build();
        return this.accountController.passwordUpdate(forgettableUser);
    }

    private TheResponse performPasswordUpdateWithUnknownUser() {
        String nickname = "the_user_nickname";
        String security_code = "security";
        when(this.userManager.getUserFromNickname(nickname)).thenReturn(null);
        TheForgettableUser forgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(nickname, security_code).build();
        return this.accountController.passwordUpdate(forgettableUser);
    }

    private TheResponse performPasswordUpdateWithInvalidUser() {
        String nickname1 = "the_user_nickname";
        String nickname2 = "the_user_nickkknameimtryingtosteal";
        String security_code = "security";
        TheUser theUser = createDummyPasswordUpdateUser(nickname2, "another_security_code");
        when(this.userManager.getUserFromNickname(nickname1)).thenReturn(theUser);
        TheForgettableUser forgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(nickname1, security_code).build();
        return this.accountController.passwordUpdate(forgettableUser);
    }

    private TheUser createDummyPasswordUpdateUser(String nickname, String security_code) {
        return UserBuilderManager.aRegularUserWithNickname(nickname)
                .withUUID(security_code)
                .build();
    }

}
