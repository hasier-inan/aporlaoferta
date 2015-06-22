package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class FeedbackController {

    private final Logger LOG = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private OfferValidatorHelper offerValidatorHelper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private OfferManager offerManager;

    @RequestMapping(value = "/votePositive", method = RequestMethod.POST)
    @ResponseBody
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TheResponse votePositive(@RequestParam(value = "offerId", required = true) String offerId) {
        TheUser theUser = obtainUserFromSession();
        TheOffer theOffer = getOfferFromId(offerId);
        if (theOffer == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_VALIDATION_ERROR, new TheResponse());
        }
        return updateOfferWithPositiveFeedback(offerId, theUser, theOffer);
    }

    @RequestMapping(value = "/voteNegative", method = RequestMethod.POST)
    @ResponseBody
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TheResponse voteNegative(@RequestParam(value = "offerId", required = true) String offerId) {
        TheUser theUser = obtainUserFromSession();
        TheOffer theOffer = getOfferFromId(offerId);
        if (theOffer == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_VALIDATION_ERROR, new TheResponse());
        }
        return updateOfferWithNegativeFeedback(offerId, theUser, theOffer);
    }

    private TheOffer getOfferFromId(String offerId) {
        try {
            return this.offerManager.getOfferFromId(Long.parseLong(offerId));
        } catch (NumberFormatException e) {
            LOG.error("Could not parse invalid offer id: ", e);
        }
        return null;
    }

    private TheResponse updateOfferWithPositiveFeedback(String offerId, TheUser theUser, TheOffer theOffer) {
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

    private boolean isGivingFeedbackToOwnOffer(TheOffer theOffer, TheUser theUser) {
        return theOffer.getOfferUser().getUserNickname().equals(theUser.getUserNickname());
    }

    private TheResponse updateOfferWithNegativeFeedback(String offerId, TheUser theUser, TheOffer theOffer) {
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

    private void assignSuccessCode(String offerId, TheUser theUser, TheResponse result) {
        String okMessage = String.format("Feedback successfully placed for offer %s from user %s",
                offerId, theUser.getUserNickname());
        LOG.info(okMessage);
        result.assignResultCode(ResultCode.ALL_OK, okMessage, "Valoración añadida");
    }

    private TheUser obtainUserFromSession() {
        String nickName = this.userManager.getUserNickNameFromSession();
        return this.userManager.getUserFromNickname(nickName);
    }

}
