package com.aporlaoferta.controller;


import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.rawmap.RequestMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTestIntegration extends ControllerTestIntegration {

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
    public void testAdminUserCanDeleteCommentAndSuccessCodeIsReceived() throws Exception {
        String commentId = createDummyComment();
        performPostRequestToDeleteComment(commentId, ADMIN_USER)
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testRegularUsersCanNotDeleteCommentAndErrorCodeIsReceived() throws Exception {
        String commentId = createDummyComment();
        performPostRequestToDeleteComment(commentId, REGULAR_USER)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeletedCommentHasStaticMessageOnIt() throws Exception {
        String commentId = createDummyComment();
        performPostRequestToDeleteComment(commentId, ADMIN_USER)
                .andExpect(status().is2xxSuccessful());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        assertEquals("Expected static message regarding to comment delete",
                theUser.obtainLatestComment().getCommentText(), ResultCode.COMMENT_DELETED_INFO.getResultDescriptionEsp());
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
        assertThat("Expected error to contain invalid user", jsonResult.get("description"), startsWith("Invalid owner found"));
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

    private String createDummyComment() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(CommentBuilderManager.aBasicCommentWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        TheOffer theOffer = theUser.obtainLatestOffer();
        String offerId = String.valueOf(theOffer.getId());
        performPostRequestToCreateComment(jsonRequest, offerId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        return String.valueOf(theUser.obtainLatestComment().getId());
    }


}