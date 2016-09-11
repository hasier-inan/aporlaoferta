package com.aporlaoferta.controller;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedbackControllerTest {

    private static final String THE_USER = "tHEuSsER";
    private static final String OFFER_ID = "31";
    @InjectMocks
    FeedbackController feedbackController;

    @Mock
    UserManager userManager;

    @Mock
    OfferManager offerManager;

    TheUser bannedUser = UserBuilderManager.aRegularUserWithNickname("banned").isEnabled(false).build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.userManager.getUserNickNameFromSession()).thenReturn(THE_USER);
        TheUser user = UserBuilderManager.aRegularUserWithNickname(THE_USER).build();
        when(this.userManager.getUserFromNickname(THE_USER)).thenReturn(user);
        when(this.offerManager.getOfferFromId(Long.parseLong(OFFER_ID))).thenReturn(
                OfferBuilderManager.aBasicOfferWithoutId().build());
        when(this.userManager.userIsBanned()).thenReturn(false);
    }

    @Test
    public void testUserCanPerformFeedbackOnlyOncePerOfferIfPositive() {
        prepareAlreadyPerformedFeedbackUserAndOffer();
        TheResponse theResponse = this.feedbackController.votePositive(OFFER_ID);
        assertThat("Expected feedback already performed code", theResponse.getCode(), is(42));
        assertThat("Expected validation error description in response", theResponse.getDescription(), startsWith("Feedback process was already performed by user and given offer"));
    }

    @Test
    public void testUserCanPerformFeedbackOnlyOncePerOfferEvenIfOppositeFeedbackIsSubmitted() {
        prepareAlreadyPerformedFeedbackUserAndOffer();
        TheResponse theResponse = this.feedbackController.voteNegative(OFFER_ID);
        assertThat("Expected feedback already performed code", theResponse.getCode(), is(42));
        assertThat("Expected validation error description in response", theResponse.getDescription(), startsWith("Feedback process was already performed by user and given offer"));
    }

    @Test
    public void testPositiveFeedbackRequestsAreForNonBannedUsersOnly() {
        when(this.userManager.getUserFromNickname(THE_USER)).thenReturn(bannedUser);
        TheResponse result = this.feedbackController.votePositive("1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testNegativeFeedbackRequestsAreForNonBannedUsersOnly() {
        when(this.userManager.getUserFromNickname(THE_USER)).thenReturn(bannedUser);
        TheResponse result = this.feedbackController.voteNegative("1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testSuccessResultIsReceivedWhenFeedbackIsCorrectlyPerformed() {
        TheResponse theResponse = this.feedbackController.votePositive(OFFER_ID);
        assertThat("Expected success code in response", theResponse.getCode(), is(0));
    }

    @Test
    public void testValidationErrorIsReceivedWhenOfferIdIsInvalid() {
        TheResponse theResponse = this.feedbackController.votePositive("sa1");
        assertThat("Expected validation error code in response", theResponse.getCode(), is(41));
        assertThat("Expected validation error description in response", theResponse.getDescription(), startsWith("Validation process failed while adding feedback"));
    }

    @Test
    public void testValidationErrorIsReceivedWhenOfferIsNotFound() {
        when(this.offerManager.getOfferFromId(Long.parseLong(OFFER_ID))).thenReturn(null);
        TheResponse theResponse = this.feedbackController.votePositive(OFFER_ID);
        assertThat("Expected validation error code in response", theResponse.getCode(), is(41));
        assertThat("Expected validation error description in response", theResponse.getDescription(), startsWith("Validation process failed while adding feedback"));
    }

    @Test
    public void testUserContainsAllOffersThatHaveGivenFeedback() {
        TheUser user = UserBuilderManager.aRegularUserWithNickname(THE_USER).build();
        when(this.userManager.getUserFromNickname(THE_USER)).thenReturn(user);
        voteThreeOffers();
        assertTrue("Expected first offer to be into the list of offers that have positive feedback",
                user.feedbackHasAlreadyBeenPerformedForOffer(30L));
        assertTrue("Expected second offer to be into the list of offers that have positive feedback",
                user.feedbackHasAlreadyBeenPerformedForOffer(50L));
        assertTrue("Expected third offer to be into the list of offers that have positive feedback",
                user.feedbackHasAlreadyBeenPerformedForOffer(70L));
        assertTrue("Expected third offer to be the unique offer with negative feedback",
                user.getNegativeOffers().size() == 1);
    }

    @Test
    public void testUserCantGiveFeedbackToOwnOffers() {
        TheUser user = UserBuilderManager.aRegularUserWithNickname(THE_USER).build();
        TheOffer theOwnOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        theOwnOffer.setOfferUser(user);
        when(this.offerManager.getOfferFromId(Long.parseLong(OFFER_ID))).thenReturn(
                theOwnOffer);
        TheResponse theResponse = this.feedbackController.votePositive(OFFER_ID);
        assertThat("Expected owner error code in response", theResponse.getCode(), is(43));
        assertThat("Expected owner error description in response", theResponse.getDescription(), startsWith("Feedback process can't be performed by offer owner"));
    }

    private void prepareAlreadyPerformedFeedbackUserAndOffer() {
        TheUser user = UserBuilderManager.aRegularUserWithNickname(THE_USER).build();
        TheOffer theOffer = OfferBuilderManager.aBasicOfferWithId(1L).build();
        when(this.offerManager.getOfferFromId(Long.parseLong(OFFER_ID))).thenReturn(theOffer);
        user.addPositiveFeedback(theOffer);
        when(this.userManager.getUserFromNickname(THE_USER)).thenReturn(user);
    }

    private void voteThreeOffers() {
        TheOffer firstOffer = OfferBuilderManager.aBasicOfferWithId(30L).build();
        TheOffer secondOffer = OfferBuilderManager.aBasicOfferWithId(50L).build();
        TheOffer thirdOffer = OfferBuilderManager.aBasicOfferWithId(70L).build();
        when(this.offerManager.getOfferFromId(Long.parseLong("30"))).thenReturn(firstOffer);
        when(this.offerManager.getOfferFromId(Long.parseLong("50"))).thenReturn(secondOffer);
        when(this.offerManager.getOfferFromId(Long.parseLong("70"))).thenReturn(thirdOffer);
        this.feedbackController.votePositive("30");
        this.feedbackController.votePositive("50");
        this.feedbackController.voteNegative("70");
    }
}
