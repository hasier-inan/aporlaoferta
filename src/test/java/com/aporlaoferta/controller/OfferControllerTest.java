package com.aporlaoferta.controller;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheOfferResponse;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferControllerTest {

    @InjectMocks
    OfferController offerController;
    @Mock
    OfferValidatorHelper offerValidatorHelper;
    @Mock
    UserManager userManager;
    @Mock
    OfferManager offerManager;
    @Mock
    CompanyManager companyManager;
    @Mock
    private CaptchaHTTPManager captchaHttpManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.userManager.getUserFromNickname(anyString())).thenReturn(UserBuilderManager.aRegularUserWithNickname("dsf").build());
        when(this.companyManager.getCompanyFromName(anyString())).thenReturn(CompanyBuilderManager.aRegularCompanyWithId(1L).build());
        when(this.captchaHttpManager.validHuman(anyString())).thenReturn(true);
    }

    @Test
    public void testReceivedResultContainsValidationErrorCodeIfValidationExceptionOccurs() {
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateOffer((TheOffer) anyObject());
        TheResponse result = this.offerController.createOffer(new TheOffer(), "recaptcha");
        assertTrue(result.getResponseResult() == ResultCode.COMMENT_VALIDATION_ERROR.getResponseResult());
        assertEquals(result.getDescription(), "Validation process failed while creating offer");
    }

    @Test
    public void testReceivedResultContainsValidationErrorCodeIfValidationExceptionOccursAlthoughNoUserIsIncluded() {
        when(this.userManager.getUserFromNickname(anyString())).thenReturn(null);
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateOffer((TheOffer) anyObject());
        TheResponse result = this.offerController.createOffer(new TheOffer(), "recaptcha");
        assertTrue(result.getResponseResult() == ResultCode.COMMENT_VALIDATION_ERROR.getResponseResult());
        assertEquals(result.getDescription(), "Validation process failed while creating offer");
    }

    @Test
    public void testUserIsSavedWithNewCreatedOffer() throws Exception {
        TheUser user = UserBuilderManager.aRegularUserWithNickname("john").build();
        when(this.userManager.getUserFromNickname(anyString())).thenReturn(user);
        when(this.userManager.saveUser(user)).thenReturn(user);
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        TheResponse result = this.offerController.createOffer(theOffer, "recaptcha");
        verify(this.userManager).saveUser(user);
        assertTrue(result.getResponseResult() == ResultCode.ALL_OK.getResponseResult());
    }

    @Test
    public void testEmptyDatabaseObjectErrorIsReceivedIfUserDoesNotContainOffer() throws Exception {
        TheUser user = UserBuilderManager.aRegularUserWithNickname("john").build();
        when(this.userManager.getUserFromNickname(anyString())).thenReturn(user);
        when(this.userManager.saveUser(user)).thenReturn(UserBuilderManager.aRegularUserWithNickname("john").build());
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithoutUserOrId().build();
        TheResponse result = this.offerController.createOffer(theOffer, "recaptcha");
        assertTrue(result.getResponseResult() == ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResponseResult());
    }

    @Test
    public void testUpdateOfferHackedForDifferentUserOfferReturnsCorrespondingError() {
        String offerId = "1";
        when(this.offerManager.getOfferFromId(Long.parseLong(offerId))).thenReturn(OfferBuilderManager.aBasicOfferWithoutId().build());
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateOffer((TheOffer) anyObject());

        TheResponse result = this.offerController.updateOffer(new TheOffer(), offerId);
        assertTrue(result.getResponseResult() == ResultCode.INVALID_OWNER_ERROR.getResponseResult());
    }

    @Test
    public void testValidationErrorCodeIsReceivedIfUpdatedOfferThrowsValidationException() throws Exception {
        when(this.userManager.getUserFromNickname(anyString())).thenReturn(null);
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateOffer((TheOffer) anyObject());
        TheResponse result = this.offerController.createOffer(new TheOffer(), "recaptcha");
        assertTrue(result.getResponseResult() == ResultCode.COMMENT_VALIDATION_ERROR.getResponseResult());
        assertEquals(result.getDescription(), "Validation process failed while creating offer");
    }

    @Test
    public void testSuccessCodeIsReturnedWhenUpdateProcessRunsCorrectly() throws Exception {
        String offerId = "1";
        String nickname = "JohnWick";
        TheOffer offerFromUser = OfferBuilderManager
                .aBasicOfferWithoutId().withUser(UserBuilderManager.aRegularUserWithNickname(nickname).build())
                .build();
        when(this.offerManager.getOfferFromId(Long.parseLong(offerId))).thenReturn(offerFromUser);
        when(this.userManager.getUserNickNameFromSession()).thenReturn(nickname);
        when(this.userManager.saveUser(offerFromUser.getOfferUser())).thenReturn(makeAProperUserWithLastOffer());
        TheResponse result = this.offerController.updateOffer(
                OfferBuilderManager.aBasicOfferWithoutId().build(), offerId);
        assertTrue(result.getResponseResult() == ResultCode.ALL_OK.getResponseResult());
    }

    @Test
    public void testNewestOffersAreReturnedInTheResponseWhenRequestedMadeWithNoNumber() throws Exception {
        List<TheOffer> sampleOfferList = createSampleOfferList();
        when(this.offerManager.getNextHundredOffers(0L)).thenReturn(sampleOfferList);
        TheOfferResponse result = this.offerController.getOffers(null);
        assertOfferListIsInResponse(result, sampleOfferList);
    }

    @Test
    public void testNextNumberOfOffersAreReturnedInTheResponseWhenRequested() throws Exception {
        List<TheOffer> sampleOfferList = createSampleOfferList();
        when(this.offerManager.getNextHundredOffers(78L)).thenReturn(sampleOfferList);
        TheOfferResponse result = this.offerController.getOffers(78L);
        assertOfferListIsInResponse(result, sampleOfferList);
    }

    @Test
    public void testHottestOffersAreReturnedInTheResponseWhenRequested() throws Exception {
        List<TheOffer> sampleOfferList = createSampleHotOfferList();
        when(this.offerManager.getNextHundredHottestOffers(0L)).thenReturn(sampleOfferList);
        TheOfferResponse result = this.offerController.getHottestOffers(0L);
        assertOfferListIsInResponse(result, sampleOfferList);
        List<TheOffer> receivedOffers = result.getTheOffers();
        Long hottest = 1000L;
        for (TheOffer theOffer : receivedOffers) {
            Assert.assertTrue("Expected offers to be sorted by hotness", theOffer.getOfferPositiveVote() < hottest);
        }
    }

    @Test
    public void FilterOffersAreReturnedInResponase() throws Exception {
        List<TheOffer> sampleOfferList = createSampleOfferList();
        when(this.offerManager.getFilteredNextHundredResults(Mockito.any(OfferFilters.class))).thenReturn(sampleOfferList);
        TheOfferResponse result = this.offerController.getFilteredOffers(new OfferFilters());
        assertOfferListIsInResponse(result, sampleOfferList);
    }

    @Test
    public void testEmptyListIsReturnedIfNoOffersAreCreated() throws Exception {
        when(this.offerManager.getNextHundredOffers(0L)).thenReturn(new ArrayList<TheOffer>());
        TheOfferResponse result = this.offerController.getOffers(null);
        Assert.assertEquals(result.getTheOffers().size(), 0);
    }

    @Test
    public void testInvalidCaptchaProcessReturnsSpecificCode() throws Exception {
        when(this.captchaHttpManager.validHuman(anyString())).thenReturn(false);
        TheResponse result = this.offerController.createOffer(null, "");
        Assert.assertEquals(result.getCode(), 75);

    }

    private void assertOfferListIsInResponse(TheOfferResponse result, List<TheOffer> expectedOffers) {
        Assert.assertThat("Expected same result of offers", result.getTheOffers().size(), is(expectedOffers.size()));
        List<TheOffer> receivedOffers = result.getTheOffers();
        for (TheOffer theOffer : receivedOffers) {
            Assert.assertTrue("Expected offer to be in the given offer list", expectedOffers.contains(theOffer));
        }
    }

    private TheUser makeAProperUserWithLastOffer() {
        Set<TheOffer> offerz = new HashSet<>();
        offerz.add(OfferBuilderManager.aBasicOfferWithoutId().build());
        return UserBuilderManager.aRegularUserWithNickname("TheNickname").withCreatedOffers(offerz).build();
    }

    private List<TheOffer> createSampleOfferList() {
        List<TheOffer> theOffers = new ArrayList<>();
        theOffers.add(OfferBuilderManager.aBasicOfferWithId(1L).build());
        theOffers.add(OfferBuilderManager.aBasicOfferWithId(2L).build());
        return theOffers;
    }

    private List<TheOffer> createSampleHotOfferList() {
        List<TheOffer> theOffers = new ArrayList<>();
        theOffers.add(OfferBuilderManager.aBasicOfferWithId(1L).withPositives(100L).build());
        theOffers.add(OfferBuilderManager.aBasicOfferWithId(2L).withPositives(300L).build());
        theOffers.add(OfferBuilderManager.aBasicOfferWithId(3L).withPositives(40L).build());
        return theOffers;
    }
}
