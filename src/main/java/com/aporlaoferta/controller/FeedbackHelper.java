package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hasiermetal on 2/02/14.
 */
public class FeedbackHelper {

    protected final Logger LOG = LoggerFactory.getLogger(FeedbackHelper.class);

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected OfferManager offerManager;

    protected TheOffer getOfferFromId(String offerId) {
        try {
            return this.offerManager.getOfferFromId(Long.parseLong(offerId));
        } catch (NumberFormatException e) {
            LOG.error("Could not parse invalid offer id: ", e);
        }
        return null;
    }

    protected TheResponse updateOfferWithPositiveFeedback(String offerId, TheUser theUser, TheOffer theOffer) {
        TheResponse result = new TheResponse();
        if (isGivingFeedbackToOwnOffer(theOffer, theUser)) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_FROM_OWNER_PERFORMED_ERROR, result);
        } else if (
                !theUser.feedbackHasAlreadyBeenPerformedForOffer(theOffer.getId())) {
            theUser.addPositiveFeedback(theOffer);
            assignSuccessCode(offerId, theUser, result);
            this.offerManager.positiveFeedback(theOffer);
            return result;
        } else {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_ALREADY_PERFORMED_ERROR, result);
        }
    }

    protected boolean isGivingFeedbackToOwnOffer(TheOffer theOffer, TheUser theUser) {
        return theOffer.getOfferUser().getUserNickname().equals(theUser.getUserNickname());
    }

    protected TheResponse updateOfferWithNegativeFeedback(String offerId, TheUser theUser, TheOffer theOffer) {
        TheResponse result = new TheResponse();
        if (isGivingFeedbackToOwnOffer(theOffer, theUser)) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_FROM_OWNER_PERFORMED_ERROR, result);
        } else if (!theUser.feedbackHasAlreadyBeenPerformedForOffer(theOffer.getId())) {
            theUser.addNegativeFeedback(theOffer);
            assignSuccessCode(offerId, theUser, result);
            this.offerManager.negativeFeedback(theOffer);
            return result;
        } else {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_ALREADY_PERFORMED_ERROR, result);
        }
    }

    protected void assignSuccessCode(String offerId, TheUser theUser, TheResponse result) {
        String okMessage = String.format("Feedback successfully placed for offer %s from user %s",
                offerId, theUser.getUserNickname());
        result.assignResultCode(ResultCode.ALL_OK, okMessage, "Valoración añadida");
    }

    protected TheUser obtainUserFromSession() {
        String nickName = this.userManager.getUserNickNameFromSession();
        return this.userManager.getUserFromNickname(nickName);
    }

}
