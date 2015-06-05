package com.aporlaoferta.controller;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.rawmap.RequestMap;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hasiermetal on 29/01/15.
 */
public class FeedbackControllerTestIntegration extends ControllerTestIntegration {


    @Test
    public void testOfferCreationIsOnlyAllowedIfIdentified() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        this.mockMvc.perform(post("/votePositive")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("_csrf", csrfToken.getToken())
                .param("offerId", "1")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testUserCanPerformFeedbackOnlyOncePerOfferIfPositive() throws Exception {
        String offerId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(offerId, true);
        ResultActions result = performPostRequestToFeedback(offerId, REGULAR_USER, true)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be duplicated feedback error", jsonResult.get("description"), startsWith("Feedback process was already performed by user and given offer"));
    }

    @Test
    public void testUserCanPerformFeedbackOnlyOncePerOfferEvenIfOppositeFeedbackIsSubmitted() throws Exception {
        String offerId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(offerId, true);
        ResultActions result = performPostRequestToFeedback(offerId, REGULAR_USER, false)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be duplicated feedback error", jsonResult.get("description"), startsWith("Feedback process was already performed by user and given offer"));
    }

    @Test
    public void testSuccessResultIsReceivedWhenFeedbackIsCorrectlyPerformed() throws Exception {
        String offerId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(offerId, true);
    }

    @Test
    public void testUserCantPerformFeedbackOverOwnOffer() throws Exception {
        //default offer created by regular user
        ResultActions result = performPostRequestToFeedback("1", REGULAR_USER, true)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to have own offer error", jsonResult.get("description"), startsWith("Feedback process can't be performed by offer owner"));
    }

    @Test
    public void testUserContainsAllOffersThatHaveGivenFeedback() throws Exception {
        String firstOfferId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(firstOfferId, true);
        String secondOfferId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(secondOfferId, true);
        String thirdOfferId = createSampleOfferAndReturnId();
        createFirstFeedbackAndAssertIsSuccess(thirdOfferId, false);
        TheUser regularUserThatPerformedFeedback = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        checkFeedbackOffersContainsOffers(regularUserThatPerformedFeedback.getPositiveOffers(), firstOfferId, secondOfferId);
        checkFeedbackOffersContainsOffers(regularUserThatPerformedFeedback.getNegativeOffers(), thirdOfferId);

    }

    private void checkFeedbackOffersContainsOffers(Set<TheOffer> feedbackOffers, String... offerIds) {
        Assert.assertThat("Expected " + offerIds.length + " feedback in the positive offer list", feedbackOffers.size(), Matchers.is(offerIds.length));
        for (TheOffer theOffer : feedbackOffers) {
            Assert.assertTrue("Expected given offerId to be part of the feedback list", Arrays.asList(offerIds).contains(theOffer.getId().toString()));
        }
    }

    @Test
    public void testInvalidOfferIdSentReturnsErrorCode() throws Exception {
        ResultActions result = performPostRequestToFeedback("invalidOfferId", REGULAR_USER, true)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while adding feedback"));
    }

    @Test
    public void testNonExistingOfferIdSentReturnsErrorCode() throws Exception {
        ResultActions result = performPostRequestToFeedback("55", REGULAR_USER, true)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while adding feedback"));
    }

    private void createFirstFeedbackAndAssertIsSuccess(String offerId, boolean isPositive) throws Exception {
        ResultActions result = performPostRequestToFeedback(offerId, REGULAR_USER, isPositive)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be a success", jsonResult.get("description"), startsWith("Feedback successfully placed for offer"));
    }
}
