package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class FeedbackController extends FeedbackHelper {

    @RequestMapping(value = "/votePositive", method = RequestMethod.POST)
    @ResponseBody
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TheResponse votePositive(@RequestParam(value = "offerId", required = true) String offerId) {
        TheUser theUser = obtainUserFromSession();
        if (!theUser.getEnabled()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }

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
        if (!theUser.getEnabled()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }

        TheOffer theOffer = getOfferFromId(offerId);
        if (theOffer == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.FEEDBACK_VALIDATION_ERROR, new TheResponse());
        }
        return updateOfferWithNegativeFeedback(offerId, theUser, theOffer);
    }

}
