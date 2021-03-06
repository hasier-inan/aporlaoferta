package com.aporlaoferta.controller;


import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.rawmap.RequestMap;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class ControllerTestIntegration {

    protected static final String REGULAR_USER = "regularUser";
    protected static final String ANOTHER_USER = "anotherUser";
    protected static final String ADMIN_USER = "theboss";

    @Autowired
    protected UserManager userManagerTest;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    protected TheUser theUser;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
        createUser(REGULAR_USER);
        createUser(ANOTHER_USER);

        if (!this.userManagerTest.doesUserExist(ADMIN_USER)) {
            this.userManagerTest.createAdminUser(UserBuilderManager.aRegularUserWithNickname(ADMIN_USER).build());
        }
        createOfferForTheUser();
    }

    private void createUser(String name) throws Exception {
        if (!this.userManagerTest.doesUserExist(name)) {
            TheUser theUser = UserBuilderManager.aRegularUserWithNickname(name).build();
            createUserFromController(theUser);
            TheUser userDetails = this.userManagerTest.getUserFromNickname(theUser.getUserNickname());
            confirmUserFromController(userDetails);
        }
    }

    protected void createOfferForTheUser() throws Exception {
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        theOffer.setOfferUser(this.theUser);
        createOfferFromController(theOffer);
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOfferUpdated = theUser.obtainLatestOffer();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        performPostRequestToCreateComment(jsonRequest, String.valueOf(theOfferUpdated.getId()), REGULAR_USER);
    }

    protected ResultActions performPostRequestToCreateComment(String jsonRequest, String offerId, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/createComment")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("offer", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performPostRequestToFeedback(String offerId, String identifiedUser, boolean isPositive) throws Exception {
        String endpoint = (isPositive) ? "/votePositive" : "/voteNegative";
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post(endpoint)
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("offerId", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performPostRequestToCreateOffer(String jsonRequest, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/createOffer")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha", "recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performAnonymousPostRequestToCreateOffer(String jsonRequest) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/createOffer")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha", "recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performPostRequestToUpdateOffer(String jsonRequest, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/updateOffer")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha", "recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }
    protected ResultActions performPostRequestToExpireOffer(String id, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/expireOffer")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("_csrf", csrfToken.getToken())
                .param("id", id)
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performPostRequestToUpdateComment(String jsonRequest, String commentId, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/updateComment")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("comment", commentId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected ResultActions performPostRequestToDeleteComment(String commentId, String identifiedUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/deleteComment")
                .with(userDeatilsService(identifiedUser))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("comment", commentId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        );
    }

    protected void createOfferFromController(TheOffer theOffer) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(theOffer);
        this.mockMvc.perform(post("/createOffer")
                .with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha", "recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        ).andExpect(status().is2xxSuccessful());
    }

    protected void createUserFromController(TheUser theUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(theUser);
        this.mockMvc.perform(post("/createUser")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha", "recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        ).andExpect(status().is2xxSuccessful());
    }

    protected void confirmUserFromController(TheUser theUser) throws Exception {
        this.mockMvc.perform(get("/confirmUser")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user", theUser.getUserNickname())
                .param("confirmationID", theUser.getUuid())
        ).andExpect(status().is2xxSuccessful());
    }

    protected String createSampleOfferAndReturnId() throws Exception {
        TheOffer basicOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        String jsonRequest = RequestMap.getJsonFromMap(basicOffer);
        ResultActions result = performPostRequestToCreateOffer(jsonRequest, ANOTHER_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        return jsonResult.get("description").substring(
                jsonResult.get("description").length() - 1,
                jsonResult.get("description").length());
    }

}