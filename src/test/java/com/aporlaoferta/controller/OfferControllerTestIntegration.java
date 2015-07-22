package com.aporlaoferta.controller;


import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.rawmap.RequestMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferControllerTestIntegration extends ControllerTestIntegration {

    @Test
    public void testOfferCreationIsOnlyAllowedIfIdentified() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(OfferBuilderManager.aBasicOfferWithoutId().build());
        this.mockMvc.perform(post("/createOffer")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .param("recaptcha","recaptcha")
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testValidationExceptionOccursIfMandatoryFieldsAreNotIncluded() throws Exception {
        TheOffer invalidOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        invalidOffer.setOfferTitle("");
        String jsonRequest = RequestMap.getJsonFromMap(invalidOffer);
        ResultActions result = performPostRequestToCreateOffer(jsonRequest, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while creating offer"));
    }

    @Test
    public void testIdentifiedUserCanCreateOfferAndSuccessCodeIsReceived() throws Exception {
        TheOffer basicOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        String jsonRequest = RequestMap.getJsonFromMap(basicOffer);
        ResultActions result = performPostRequestToCreateOffer(jsonRequest, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("Offer successfully updated"));
    }

    @Test
    public void testIdentifiedUserCanUpdateOwnOfferAndSuccessCodeIsReceived() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(OfferBuilderManager.aBasicOfferWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String originalOfferId = String.valueOf(this.theUser.obtainLatestOffer().getId());
        ResultActions result = performPostRequestToUpdateOffer(jsonRequest, originalOfferId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be ok", jsonResult.get("description"), startsWith("Offer successfully updated"));
    }

    @Test
    public void testUpdateOfferThatHasDifferentOwnerReturnsErrorCode() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(OfferBuilderManager.aBasicOfferWithoutId().build());
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String regularUserOfferId = String.valueOf(this.theUser.obtainLatestOffer().getId());
        ResultActions result = performPostRequestToUpdateOffer(jsonRequest, regularUserOfferId, ANOTHER_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected error to contain invalid user", jsonResult.get("description"), startsWith("Invalid owner found"));
    }

    @Test
    public void testUpdateOfferWithMissingMandatoryDataReturnsValidationError() throws Exception {
        TheOffer invalidOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        invalidOffer.setOfferTitle("");
        String jsonRequest = RequestMap.getJsonFromMap(invalidOffer);
        this.theUser = this.userManagerTest.getUserFromNickname(REGULAR_USER);
        String originalOfferId = String.valueOf(this.theUser.obtainLatestOffer().getId());
        ResultActions result = performPostRequestToUpdateOffer(jsonRequest, originalOfferId, REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while creating offer"));
    }

    @Test
    public void testUpdateOfferWithNonexistingOfferIdReturnsValidationError() throws Exception {
        TheOffer invalidOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        invalidOffer.setOfferTitle("");
        String jsonRequest = RequestMap.getJsonFromMap(invalidOffer);
        ResultActions result = performPostRequestToUpdateOffer(jsonRequest, "666", REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while updating offer"));
    }

    @Test
    public void testUpdateOfferWithInvalidOfferIdReturnsValidationError() throws Exception {
        String jsonRequest = RequestMap.getJsonFromMap(OfferBuilderManager.aBasicOfferWithoutId().build());
        ResultActions result = performPostRequestToUpdateOffer(jsonRequest, "6f66", REGULAR_USER)
                .andExpect(status().is2xxSuccessful());
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        assertThat("Expected result to be validation error", jsonResult.get("description"), startsWith("Validation process failed while updating offer"));
    }

    @Test
    public void testOnlyIdentifiedUsersCanRequestOfferUpdate() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(OfferBuilderManager.aBasicOfferWithoutId().build());
        String offerId = "444";
        this.mockMvc.perform(post("/updateOffer")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("offerId", offerId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testNonIdentifiedUsersCanRequestOfferFilter() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        String jsonRequest = RequestMap.getJsonFromMap(FilterBuilderManager.anAllElectronicsFilterWithText("asdf").build());
        this.mockMvc.perform(post("/getFilteredOffers")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testAnonymousUsersCanRequestOfferLists() throws Exception {
        makeAGetOfferRequestCallAndAssertIsSuccessful();
    }

    @Test
    public void testCreatedOffersAreShownInTheList() throws Exception {
        String oneOffer = createSampleOfferAndReturnId();
        String anotherOffer = createSampleOfferAndReturnId();
        ResultActions result = makeAGetOfferRequestCallAndAssertIsSuccessful();
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        assertResultContainsBothOffers(oneOffer, anotherOffer, mvcResult);
    }

    @Test
    public void testGetOfferByIdDoesIncludeAllCommentsInIt() throws Exception {
        ResultActions result = makeAGetOfferByIdRequestCallAndAssertIsSuccessful("1");
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> jsonResult = RequestMap.getOfferMapFromJsonString(mvcResult);
        List<Map<String, Object>> offerList = (ArrayList) jsonResult.get("theOffers");
        for (Map<String, Object> offer : offerList) {
            Assert.assertNotNull(offer.get("offerComments").toString());
        }
    }

    @Test
    public void testGetOfferListShowsNoPersonalInformationOfOwner() throws Exception {
        ResultActions result = makeAGetOfferByIdRequestCallAndAssertIsSuccessful("1");
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> jsonResult = RequestMap.getOfferMapFromJsonString(mvcResult);
        List<Map<String, Object>> offerList = (ArrayList) jsonResult.get("theOffers");
        for (Map<String, Object> offer : offerList) {
            Map<String, String> offerUser = (Map) offer.get("offerUser");
            assertNoPersonalInformationIsReceivedInMap(offerUser);
            assertNoPersonalInformationIsReceivedInComments(offer);
        }
    }

    @Test
    public void testCreatedOffersContainNoCommentsAndNoUserPersonalInfo() throws Exception {
        createSampleOfferAndReturnId();
        createSampleOfferAndReturnId();
        ResultActions result = makeAGetOfferRequestCallAndAssertIsSuccessful();
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, Object> jsonResult = RequestMap.getOfferMapFromJsonString(mvcResult);
        List<Map<String, Object>> offerList = (ArrayList) jsonResult.get("theOffers");
        for (Map<String, Object> offer : offerList) {
            List offerComments = (List) offer.get("offerComments");
            Assert.assertTrue("Expected no comments", offerComments.size() == 0);
            assertNoPersonalInformationIsReceivedInMap((Map) offer.get("offerUser"));
        }
    }

    @Test
    public void testOfferCategoriesAreReturned() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        ResultActions result = this.mockMvc.perform(post("/getOfferCategories")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
        assertAllCategoriesAreReturned(result);
    }

    private void assertAllCategoriesAreReturned(ResultActions result) throws java.io.IOException {
        String mvcResult = result.andReturn().getResponse().getContentAsString();
        Map<String, String> jsonResult = RequestMap.getMapFromJsonString(mvcResult);
        OfferCategory[] categories = OfferCategory.values();
        for (OfferCategory category : categories) {
            Assert.assertTrue("Expected category to inside the map", jsonResult.containsKey(category.name()));
        }
    }

    private void assertNoPersonalInformationIsReceivedInComments(Map<String, Object> offer) {
        for (Map<String, Object> offerComments : (List<Map>) offer.get("offerComments")) {
            Map<String, String> commentOwner = (Map) offerComments.get("commentOwner");
            assertNoPersonalInformationIsReceivedInMap(commentOwner);
        }
    }

    private void assertNoPersonalInformationIsReceivedInMap(Map<String, String> offerUser) {
        Assert.assertNull(offerUser.get("userPassword"));
        Assert.assertNull(offerUser.get("userEmail"));
    }

    private void assertResultContainsBothOffers(String oneOffer, String anotherOffer, String mvcResult) throws java.io.IOException {
        Map<String, Object> jsonResult = RequestMap.getOfferMapFromJsonString(mvcResult);
        List<Map<String, Object>> offerList = (ArrayList) jsonResult.get("theOffers");
        boolean oneFound = false;
        boolean anotherFound = false;
        for (Map<String, Object> offer : offerList) {
            oneFound = oneFound || oneOffer.equals(offer.get("id").toString());
            anotherFound = anotherFound || anotherOffer.equals(offer.get("id").toString());
        }
        Assert.assertTrue("Expected both created offers to be in the list of offers got in the reponse",
                oneFound && anotherFound);
    }

    private ResultActions makeAGetOfferRequestCallAndAssertIsSuccessful() throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/getOffers")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }

    private ResultActions makeAGetOfferByIdRequestCallAndAssertIsSuccessful(String id) throws Exception {
        CsrfToken csrfToken = CsrfTokenBuilder.generateAToken();
        return this.mockMvc.perform(post("/getOffer")
                .sessionAttr("_csrf", csrfToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("_csrf", csrfToken.getToken())
                .param("id", id)
                .sessionAttrs(SessionAttributeBuilder
                        .getSessionAttributeWithHttpSessionCsrfTokenRepository(csrfToken))
        )
                .andExpect(status().is2xxSuccessful());
    }
}