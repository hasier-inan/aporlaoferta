package com.aporlaoferta.controller;


import com.aporlaoferta.data.ForgettableUserBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheForgettableUser;
import com.aporlaoferta.model.TheNewUser;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.rawmap.RequestMap;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.aporlaoferta.controller.SecurityRequestPostProcessors.userDeatilsService;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.StringUtils.isEmpty;


@ContextConfiguration(
        loader = WebContextLoader.class, value = {
        "classpath:mvc-dispatcher-test-servlet.xml",
        "classpath:aporlaoferta-controller-test-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerRealTestIntegration {

    private static final String REGULAR_USER = "regularUser";
    private static final String ADMIN_USER = "imtheboss";
    private static final String REGULAR_ENCODED_USER = "TYO";

    @Autowired
    UserManager userManagerTest;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private String uncodedPassword = "123456789";

    @Before
    public void setup() {
        if (!this.userManagerTest.doesUserExist(REGULAR_USER)) {
            this.userManagerTest.createUser(UserBuilderManager.aRegularUserWithNickname(REGULAR_USER)
                    .withPassword(uncodedPassword).build());
        }
        if (!this.userManagerTest.doesUserExist(ADMIN_USER)) {
            this.userManagerTest.createAdminUser(UserBuilderManager.aRegularUserWithNickname(ADMIN_USER).build());
        }
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    ////// admin

    @Test
    public void testForbiddenAccessToRegularUser() throws Exception {
        this.mockMvc.perform(get("/admin").with(userDeatilsService(REGULAR_USER)))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    public void testForbiddenAccessToAnonymousUser() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        ;
    }

    @Test
    public void testAccessGrantedForAdminUser() throws Exception {
        this.mockMvc.perform(get("/admin").with(userDeatilsService(ADMIN_USER)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/admin.jsp"));
    }

    ////// 403

    @Test
    public void testForbiddenEndpointAddsLoggedUserDataInModel() throws Exception {
        this.mockMvc.perform(get("/403").with(userDeatilsService(ADMIN_USER)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/403.jsp"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    public void testForbiddenEndpointAddsNoUserDataInModelIfAnonymous() throws Exception {
        this.mockMvc.perform(get("/403"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/view/403.jsp"))
                .andExpect(model().attributeDoesNotExist("username"));
    }

    ////// create user
    @Test
    public void testCreateUserIsNotRestrictedToAnonymousUsers() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(UserBuilderManager.aRegularUserWithNickname("moko").build());
        ResultActions result = getResultActionsForCreateUser(csrfToken, jsonRequest);
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("User successfully created"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testCreateUserInitialisesThePendingAccountToTrue() throws Exception {
        String nickname = "moko";
        TheUser userMoko = UserBuilderManager.aPendingUserWithNickname(nickname).build();
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(userMoko);
        getResultActionsForCreateUser(csrfToken, jsonRequest);
        getResultActionsForUserDetails(csrfToken, nickname).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testCreateUserShowsNoUUID() throws Exception {
        String nickname = "moko";
        TheUser userMoko = UserBuilderManager.aPendingUserWithNickname(nickname).build();
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(userMoko);
        ResultActions resultActionsForCreateUser = getResultActionsForCreateUser(csrfToken, jsonRequest);
        String createdUser = resultActionsForCreateUser.andReturn().getResponse().getContentAsString();
        Map<String, String> createdUserMap = RequestMap.getMapFromJsonString(createdUser);
        assertTrue("Expected no uuid value", (isEmpty(createdUserMap.get("uuid"))));
    }

    @Test
    public void testCreateUserIsUpdatedToPendingFalse() throws Exception {
        String nickname = "moko";
        TheUser userMoko = UserBuilderManager.aPendingUserWithNickname(nickname).build();
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(userMoko);
        getResultActionsForCreateUser(csrfToken, jsonRequest);
        TheUser theUser = this.userManagerTest.getUserFromNickname(nickname);
        getResultActionsForUserConfirmed(theUser.getUuid(), theUser.getUserNickname()).andReturn().getResponse().getContentAsString();
        String result = getResultActionsForUserDetails(csrfToken, nickname).andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(result);
        assertTrue("Expected pending flag to be false", "false".equals(jsonResult.get("pending")));
    }

    private ResultActions getResultActionsForUserConfirmed(String uuid, String nickname) throws Exception {
        return this.mockMvc.perform(get("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("confirmationID", uuid)
                        .param("user", nickname)
        )
                .andExpect(status().is2xxSuccessful());
    }

    private ResultActions getResultActionsForCreateUser(CsrfToken csrfToken, String jsonRequest) throws Exception {
        return this.mockMvc.perform(post("/createUser")
                        //.with(userDeatilsService(REGULAR_USER))
                        .sessionAttr("_csrf", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("_csrf", csrfToken.getToken())
                        .param("recaptcha", "recaptcha")
                        .sessionAttrs(SessionAttributeBuilder
                                .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    private ResultActions getResultActionsForUserDetails(CsrfToken csrfToken, String sessionNickname) throws Exception {
        return this.mockMvc.perform(post("/accountDetails")
                        .with(userDeatilsService(sessionNickname))
                        .sessionAttr("_csrf", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("_csrf", csrfToken.getToken())
                        .sessionAttrs(SessionAttributeBuilder
                                .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    private ResultActions getResultActionsForForgottenPassword(CsrfToken csrfToken,
                                                               String jsonRequest) throws Exception {
        return this.mockMvc.perform(post("/forgottenPassword")
                        .sessionAttr("_csrf", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("_csrf", csrfToken.getToken())
                        .content(jsonRequest)
                        .sessionAttrs(SessionAttributeBuilder
                                .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testAccessIsAllowedForAnonymousUserIfForgottenPassword() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        TheForgettableUser theUser = ForgettableUserBuilderManager.aForgettableUserWithNicknameAndCode(
                "moo", "moo"
        ).build();
        String jsonRequest = RequestMap.getJsonFromMap(theUser);
        getResultActionsForForgottenPassword(csrfToken, jsonRequest);
    }


    @Test
    public void testUpdateUserWithIncorrectOldPasswordReturnsExpectedCode() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        TheNewUser theUser = createDummyNewUserWithInvalidOldPassword();
        String jsonRequest = RequestMap.getJsonFromMap(theUser);
        ResultActions result = getResultActions(csrfToken, jsonRequest, REGULAR_USER);
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be USER_NAME_PASSWORD_INVALID", jsonResult.get("description"), startsWith("Provided user password is invalid"));
    }

    @Test
    public void testUpdateUserIsRestrictedToIdentifiedUsers() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(UserBuilderManager.aRegularUserWithNickname("moko").build());
        this.mockMvc.perform(post("/updateUser")
                        .sessionAttr("_csrf", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("_csrf", csrfToken.getToken())
                        .param("recaptcha", "recaptcha")
                        .sessionAttrs(SessionAttributeBuilder
                                .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testUpdateUserIsCorrectlyPerformedForRegularUser() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequestCreate = RequestMap.getJsonFromMap(createUserWithUnencodedPassword());
        getResultActionsForCreateUser(csrfToken, jsonRequestCreate);
        String jsonRequest = RequestMap.getJsonFromMap(createDummyNewUserWithValidOldPassword());
        ResultActions result = getResultActions(csrfToken, jsonRequest, REGULAR_ENCODED_USER);
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("User successfully updated"));
    }

    //forgotten password
    @Test
    public void testForgottenPasswordIsPerformedWithRequiredValidData() throws Exception {
        String dasNickName = "dasNickName";
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        TheUser oldUser = createUserBeforeHeForgetsThePassword(dasNickName, csrfToken);
        String passOld = oldUser.getUserPassword();
        String theUUID = oldUser.getUuid();
        //forgot pass
        TheForgettableUser theForgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(dasNickName, theUUID).build();
        assertUserPerformsPasswordUpdateCorrectly(csrfToken, oldUser, passOld, theForgettableUser);
    }

    @Test
    public void testForgottenPasswordReceivesInvalidCodeIfUUIDMismatch() throws Exception {
        String dasNickName = "dasNickName";
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        createUserBeforeHeForgetsThePassword(dasNickName, csrfToken);
        String theUUID = "anotherUUID";
        assertIncorrectValuesReturnsInvalidResult(dasNickName, csrfToken, theUUID);
    }

    @Test
    public void testForgottenPasswordReceivesInvalidCodeIfUserNicknameMismatch() throws Exception {
        String dasNickName = "dasNickName";
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        TheUser oldUser = createUserBeforeHeForgetsThePassword(dasNickName, csrfToken);
        String theUUID = oldUser.getUuid();
        assertIncorrectValuesReturnsInvalidResult("differentNickname", csrfToken, theUUID);
    }

    @Test
    public void testForgottenPasswordReceivesInvalidCodeIfPasswordsMismatch() throws Exception {
        String dasNickName = "dasNickName";
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        TheUser oldUser = createUserBeforeHeForgetsThePassword(dasNickName, csrfToken);
        String theUUID = oldUser.getUuid();
        assertDifferentPasswordsTriggerCorrectInvalidCode(dasNickName, csrfToken, theUUID);
    }

    private void assertDifferentPasswordsTriggerCorrectInvalidCode(String dasNickName, CsrfToken csrfToken, String theUUID) throws Exception {
        TheForgettableUser theForgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(dasNickName, theUUID)
                .withPass1("pa1")
                .withPass2("pa2")
                .build();
        Map<String, String> jsonResult = obtainResult(csrfToken, theForgettableUser);
        assertEquals("Expected result to be \'username password invalid\'", jsonResult.get("code"), "4");
    }

    private Map<String, String> obtainResult(CsrfToken csrfToken, TheForgettableUser theForgettableUser) throws Exception {
        ResultActions resultActions = getResultActionsForForgottenPassword(csrfToken,
                RequestMap.getJsonFromMap(theForgettableUser));
        String mvcResult = resultActions.andReturn().getResponse().getContentAsString();
        return RequestMap.getMapFromJsonString(mvcResult);
    }

    private void assertIncorrectValuesReturnsInvalidResult(String dasNickName, CsrfToken csrfToken, String theUUID) throws Exception {
        TheForgettableUser theForgettableUser = ForgettableUserBuilderManager
                .aForgettableUserWithNicknameAndCode(dasNickName, theUUID).build();
        Map<String, String> jsonResult = obtainResult(csrfToken, theForgettableUser);
        assertEquals("Expected result to be \'invalid password verification\'", jsonResult.get("code"), "5");
    }

    private void assertUserPerformsPasswordUpdateCorrectly(CsrfToken csrfToken, TheUser oldUser, String passOld, TheForgettableUser theForgettableUser) throws Exception {
        Map<String, String> jsonResult = obtainResult(csrfToken, theForgettableUser);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("User successfully updated"));
        String passNew = this.userManagerTest.getUserFromNickname(oldUser.getUserNickname()).getUserPassword();
        assertThat("Excected password to be updated", passOld, not(passNew));
    }

    private TheUser createUserBeforeHeForgetsThePassword(String dasNickName, CsrfToken csrfToken) throws Exception {
        TheUser theUser = UserBuilderManager
                .aRegularUserWithNickname(dasNickName)
                .build();
        String jsonRequest = RequestMap.getJsonFromMap(theUser);
        getResultActionsForCreateUser(csrfToken, jsonRequest);
        return this.userManagerTest.getUserFromNickname(
                theUser.getUserNickname());
    }

    private TheUser createUserWithUnencodedPassword() {
        return UserBuilderManager.aRegularUserWithNickname(REGULAR_ENCODED_USER).withPassword(uncodedPassword).build();
    }

    private ResultActions getResultActions(CsrfToken csrfToken, String jsonRequest, String user) throws Exception {
        return this.mockMvc.perform(post("/updateUser")
                        .with(userDeatilsService(user))
                        .sessionAttr("_csrf", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .param("_csrf", csrfToken.getToken())
                        .param("recaptcha", "recaptcha")
                        .sessionAttrs(SessionAttributeBuilder
                                .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    private TheNewUser createDummyNewUserWithValidOldPassword() {
        TheNewUser theNewUser = new TheNewUser();
        theNewUser.setOldPassword(uncodedPassword);
        theNewUser.setUserEmail("sdfasd");
        theNewUser.setUserAvatar("sdfasd");
        theNewUser.setUserPassword("sdfasd");
        theNewUser.setUserNickname(REGULAR_ENCODED_USER);
        return theNewUser;
    }

    private TheNewUser createDummyNewUserWithInvalidOldPassword() {
        TheNewUser theNewUser = new TheNewUser();
        theNewUser.setOldPassword(uncodedPassword + "wrong");
        theNewUser.setUserEmail("sdfasd");
        theNewUser.setUserAvatar("sdfasd");
        theNewUser.setUserPassword("sdfasd");
        theNewUser.setUserNickname("sdfasd");
        return theNewUser;
    }

}