package com.aporlaoferta.controller;


import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.rawmap.RequestMap;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.Test;
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
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(
        loader = WebContextLoader.class,
        value = {
                "classpath:mvc-dispatcher-test-servlet.xml",
                "classpath:aporlaoferta-controller-test-context.xml"
        })
@RunWith(SpringJUnit4ClassRunner.class)
public class CommentControllerTestIntegration {

    private static final String REGULAR_USER = "regularUser";
    private static final String ANOTHER_USER = "anotherUser";

    @Autowired
    UserManager userManagerTest;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private TheUser theUser;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
        if (!this.userManagerTest.doesUserExist(REGULAR_USER)) {
            TheUser theUser = UserBuilderManager.aRegularUserWithNickname(REGULAR_USER).build();
            createUserFromController(theUser);
        }
        if (!this.userManagerTest.doesUserExist(ANOTHER_USER)) {
            TheUser theUser = UserBuilderManager.aRegularUserWithNickname(ANOTHER_USER).build();
            createUserFromController(theUser);
        }
        createOfferForTheUser();
    }

    @Test
    public void testCommentCreationIsOnlyAllowedIfIdentified() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithId(2L).build());
        String offerId = "1";
        this.mockMvc.perform(post("/createComment")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("offer", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testValidationExceptionOccursIfOfferIsNotFound() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        String offerId = "9908";
        ResultActions result = performPostRequestToCreateComment(jsonRequest, offerId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while creating comment"));
    }

    @Test
    public void testValidationExceptionOccursIfMandatoryFieldsAreNotIncluded() throws Exception {
        OfferComment comment = CommentBuilderManager.aBasicCommentWithoutId().build();
        comment.setCommentText("");
        String jsonRequest = RequestMap.getJsonFromMap(comment);
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOffer = theUser.obtainLatestOffer();
        String offerId = String.valueOf(theOffer.getId());
        ResultActions result = performPostRequestToCreateComment(jsonRequest, offerId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while creating comment"));
    }

    @Test
    public void testIdentifiedUSerCanCreateCommentAndSuccessCodeIsReceived() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOffer = theUser.obtainLatestOffer();
        String offerId = String.valueOf(theOffer.getId());
        ResultActions result = performPostRequestToCreateComment(jsonRequest, offerId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("Comment successfully created"));
    }

    @Test
    public void testIdentifiedUserCanUpdateOwnCommentAndSuccessCodeIsReceived() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String commentId = String.valueOf(theUser.obtainLatestComment().getId());
        ResultActions result = performPostRequestToUpdateComment(jsonRequest, commentId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("Comment successfully created"));
    }

    @Test
    public void testUpdateCommentThatHasDifferentOwnerReturnsErrorCode() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String commentId = String.valueOf(this.theUser.obtainLatestComment().getId());
        ResultActions result = performPostRequestToUpdateComment(jsonRequest, commentId, ANOTHER_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected error to contain invalid user", jsonResult.get("description"), startsWith("Provided comment is not valid for the user"));
    }

    @Test
    public void testUpdateCommentWithMissingMandatoryDataReturnsValidationError() throws Exception {
        OfferComment comment = CommentBuilderManager.aBasicCommentWithoutId().build();
        comment.setCommentText("");
        String jsonRequest = RequestMap.getJsonFromMap(comment);
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String commentId = String.valueOf(theUser.obtainLatestComment().getId());
        ResultActions result = performPostRequestToUpdateComment(jsonRequest, commentId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while creating comment"));
    }

    @Test
    public void testOnlyIdentifiedUsersCanRequestCommentUpdate() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithId(2L).build());
        String commentId = "1";
        this.mockMvc.perform(post("/updateComment")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("comment", commentId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    private void createOfferForTheUser() throws Exception {
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        theOffer.setOfferUser(this.theUser);
        createOfferFromController(theOffer);
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOfferUpdated = theUser.obtainLatestOffer();
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        performPostRequestToCreateComment(jsonRequest, String.valueOf(theOfferUpdated.getId()), REGULAR_USER);
    }

    private ResultActions performPostRequestToCreateComment(String jsonRequest, String offerId, String identifiedUser) throws Exception {
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

    private ResultActions performPostRequestToUpdateComment(String jsonRequest, String commentId, String identifiedUser) throws Exception {
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

    private void createOfferFromController(TheOffer theOffer) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(theOffer);
        this.mockMvc.perform(post("/createOffer")
                .with(userDeatilsService(REGULAR_USER))
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        ).andExpect(status().is2xxSuccessful());
    }

    private void createUserFromController(TheUser theUser) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(theUser);
        this.mockMvc.perform(post("/createUser")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        ).andExpect(status().is2xxSuccessful());
    }

}